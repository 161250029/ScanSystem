from keras.models import Sequential, Model
from keras.layers import Conv1D, MaxPool1D, GlobalAveragePooling1D, Dropout
from keras.layers import Input, Dense, Flatten, LSTM
from keras.callbacks import Callback
from keras_gcn import GraphConv
from Generator import *
from Config import get_config
import os
import random
import json
import sys
import itertools
import pandas as pd
import numpy as np


def cnn_model(size):
    model = Sequential()
    model.add(Conv1D(32, kernel_size=3, activation='relu', input_shape=(get_config("seq_len"), size)))
    model.add(MaxPool1D(2))
    model.add(Conv1D(16, kernel_size=5, activation='relu'))
    model.add(GlobalAveragePooling1D())
    model.add(Dropout(0.5))
    model.add(Dense(64, activation='relu'))
    model.add(Dense(2, activation='softmax'))
    model.compile(optimizer='adam',
              loss='categorical_crossentropy',
              metrics=['accuracy'])
    return model


def lstm_model(size):
    model = Sequential()
    model.add(LSTM(size, input_shape=(get_config("seq_len"), size)))
    model.add(Dense(32, activation='relu'))
    model.add(Dense(16, activation='relu'))
    model.add(Dense(2, activation='softmax'))
    model.compile(loss='categorical_crossentropy',
                  optimizer='adam',
                  metrics=['accuracy'])
    return model


def gcn_model(size):
    unit_num = 4
    node_input = Input(shape=(get_config("node_len"), size), name='Input-Node')
    edge_input = Input(shape=(get_config("node_len"), get_config("node_len")), dtype='int32', name='Input-Edge')
    conv_layer = GraphConv(units=unit_num, name='GraphConv')([node_input, edge_input])
    x = Flatten()(conv_layer)
    x = Dense(unit_num * get_config("node_len"), activation='relu')(x)
    x = Dense(64, activation='relu')(x)
    output_layer = Dense(2, activation='softmax')(x)
    model = Model(inputs=[node_input, edge_input], outputs=output_layer)
    model.compile(loss='categorical_crossentropy',
                  optimizer='adam',
                  metrics=['accuracy'])
    return model


if __name__ == "__main__":

    if len(sys.argv) < 2:
        print(json.dumps({"modelNum": 4}))
    else:
        vector_paths = get_feature_files(sys.argv[1])
        random.shuffle(vector_paths)

        deepwalk_files = get_feature_files(sys.argv[1], "DeepWalk")
        random.shuffle(deepwalk_files)
        paragraph_files = get_feature_files(sys.argv[1], "ParagraphVec")
        random.shuffle(paragraph_files)
        # print(vector_size, len(deepwalk_files))

        model_path = sys.argv[2]
        model_name = sys.argv[3]
        epoch_num = int(sys.argv[4])
        feature_size = int(sys.argv[5])

        if len(sys.argv) > 5:
            vector_paths_test = vector_paths[:int(len(vector_paths) * 0.2)]
            vector_paths = vector_paths[int(len(vector_paths) * 0.2):]
            deepwalk_files_test = deepwalk_files[:int(len(deepwalk_files) * 0.2)]
            deepwalk_files = deepwalk_files[int(len(deepwalk_files) * 0.2):]
            paragraph_files_test = paragraph_files[:int(len(paragraph_files) * 0.2)]
            paragraph_files = paragraph_files[int(len(paragraph_files) * 0.2):]
        else:
            vector_paths_test = vector_paths
            deepwalk_files_test = deepwalk_files
            paragraph_files_test = paragraph_files

        model_list = [{
            "name": "cnn",
            "model": cnn_model(feature_size),
            "generator": vector_generate(sys.argv[1], vector_paths, get_config("seq_len")),
            "steps": int(len(vector_paths) / get_config("batch_size")),
            "test_generator": vector_generate(sys.argv[1], vector_paths_test, get_config("seq_len")),
            "test_steps": int(len(vector_paths_test) / get_config("batch_size")),
        },{
            "name": "lstm",
            "model": lstm_model(feature_size),
            "generator": vector_generate(sys.argv[1], vector_paths, get_config("seq_len")),
            "steps": int(len(vector_paths) / get_config("batch_size")),
            "test_generator": vector_generate(sys.argv[1], vector_paths_test, get_config("seq_len")),
            "test_steps": int(len(vector_paths_test) / get_config("batch_size")),
        },{
            "name": "deepwalk",
            "model": gcn_model(feature_size),
            "generator": graph_generate(sys.argv[1], "DeepWalk", deepwalk_files, get_config("node_len")),
            "steps": int(len(deepwalk_files) / get_config("batch_size")),
            "test_generator": graph_generate(sys.argv[1], "DeepWalk", deepwalk_files_test, get_config("node_len")),
            "test_steps": int(len(deepwalk_files_test) / get_config("batch_size")),
        },{
            "name": "para2vec",
            "model": gcn_model(feature_size),
            "generator": graph_generate(sys.argv[1], "ParagraphVec", paragraph_files, get_config("node_len")),
            "steps": int(len(paragraph_files) / get_config("batch_size")),
            "test_generator": graph_generate(sys.argv[1], "ParagraphVec", paragraph_files_test, get_config("node_len")),
            "test_steps": int(len(paragraph_files_test) / get_config("batch_size")),
        }]

        for item in model_list:
            if model_name != item["name"]:
                continue
            model = item["model"]

            if len(sys.argv) > 5:
                g1, g2, g3 = itertools.tee(item["test_generator"], 3)
                history = model.fit_generator(item["generator"], steps_per_epoch=item["steps"],
                                              validation_data=g1, validation_steps=item["test_steps"],
                                              epochs=epoch_num, verbose=0, workers=0).history
                predict_result = None
                for i in range(item["test_steps"]):
                    predict_y = model.predict_generator(g2, steps=1, workers=0, verbose=0)
                    label_y = g3.__next__()[1]
                    if predict_result is None:
                        predict_result = np.concatenate((predict_y, label_y), axis=1)
                    else:
                        predict_result = np.concatenate((predict_result, np.concatenate((predict_y, label_y), axis=1)))
                # print(predict_result)
                pd.DataFrame(predict_result)\
                    .to_csv(os.path.join(model_path, item["name"] + ".csv"), header=False, index=False)
            # else:
            #    history = model.fit_generator(item["generator"], steps_per_epoch=item["steps"],
            #                                  epochs=epoch_num, verbose=0).history
            history["name"] = item["name"]
            model.save(os.path.join(model_path, "_".join([item["name"], str(feature_size), str(epoch_num)]) + ".h5"))
            with open(os.path.join(model_path, "_".join([item["name"], str(feature_size), str(epoch_num)])), 'w') as f:
                f.write(str(history).replace("'", "\""))
                f.close
