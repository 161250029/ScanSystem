from keras.engine.saving import load_model
from keras_gcn import GraphConv
from Generator import *
from Config import get_config
import os
import sys
import pandas as pd
import numpy as np

seq_len = 256


def model_predict(model, data_generator, all_steps, label_generator=None):
    result = None
    for i in range(all_steps):
        predict_y = model.predict_generator(data_generator, steps=1, workers=0, verbose=0)
        if label_generator is not None:
            label_y = label_generator.__next__()[1]
            if result is None:
                result = np.concatenate((predict_y, label_y), axis=1)
            else:
                result = np.concatenate((result, np.concatenate((predict_y, label_y), axis=1)))
        else:
            if result is None:
                result = np.array(predict_y)
            else:
                result = np.concatenate((result, np.array(predict_y)))
    return result


def read_wordVector(path, type):
    x_data = None
    file_name = []
    for f in os.listdir(os.path.join(path, type)):
        data = pd.read_csv(os.path.join(path, type, f), header=None)
        line = np.array(data.values)

        line = np.pad(line, ((0, seq_len-line.shape[0]), (0, 0)))
        if x_data is not None:
            x_data = np.concatenate((x_data, [line]))
        else:
            x_data = np.array([line])
        file_name.append(f)

    return x_data, file_name


if __name__ == "__main__":
    model_path = sys.argv[1]
    result_dict = {}

    for f in os.listdir(model_path):
        if len(f.split(".")) != 2 or f.split(".")[1] != "h5":
            continue

        model_name = f.split(".")[0]

        if model_name == "CNN" or model_name == "LSTM":
            model = load_model(os.path.join(model_path, f))
            predict_files = get_feature_files(model_path)
            predict_generator = vector_generate(sys.argv[1], predict_files, get_config("seq_len"))
        elif model_name == "DeepWalk_GCN":
            model = load_model(os.path.join(model_path, f), custom_objects={"GraphConv": GraphConv})
            predict_files = get_feature_files(model_path, "DeepWalk")
            predict_generator = graph_generate(sys.argv[1], "DeepWalk", predict_files, get_config("node_len"))
        elif model_name == "Paragraph2Vec_GCN":
            model = load_model(os.path.join(model_path, f), custom_objects={"GraphConv": GraphConv})
            predict_files = get_feature_files(model_path, "ParagraphVec")
            predict_generator = graph_generate(sys.argv[1], "ParagraphVec", predict_files, get_config("node_len"))
        else:
            continue

        predict_result = model_predict(model, predict_generator, int(len(predict_files) / get_config("batch_size")) + 1)
        for index, row in enumerate(predict_result):
            if index < len(predict_files):
                file_name = predict_files[index][0].split(".")[0]
                if file_name not in result_dict:
                    result_dict[file_name] = {}
                    result_dict[file_name]["name"] = file_name
                result_dict[file_name][model_name] = row[0]
            # data, file_names = read_wordVector(model_path, "WordVector")
            #
            # for index, file_name in enumerate(file_names):
            #     if file_name not in result_dict:
            #         result_dict[file_name] = {}
            #         result_dict[file_name]["name"] = file_name.split(".")[0]
            #     result_dict[file_name][model_name] = predict_y[index][0]

    result = str(list(result_dict.values()))
    print(result.replace("'", "\""))