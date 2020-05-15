from keras.models import Sequential, Model
from keras.layers import Conv1D, MaxPool1D, GlobalAveragePooling1D, Dropout
from keras.layers import Input, Dense, Flatten, LSTM
from keras.callbacks import Callback
from keras_gcn import GraphConv
from Config import get_config
import os
import random
import json
import sys
import itertools
import pandas as pd
import numpy as np


def get_feature_files(output_path, node_type="WordVector"):
    result = []
    for label in ["False", "Positive"]:
        if not os.path.exists(os.path.join(output_path, label, "WordVector")):
            continue
        if node_type == "WordVector":
            for f in os.listdir(os.path.join(output_path, label, "WordVector")):
                result.append((f, label))
        else:
            for f in os.listdir(os.path.join(output_path, label, "Edge")):
                if os.path.isfile(os.path.join(output_path, label, node_type, f)):
                    result.append((f, label))
    return result


def feature_generate(output_path, feature_type, files, len_limit):
    num = 0
    while True:
        x = []
        y = []
        for file_name, label in files:
            file_path = os.path.join(output_path, label, feature_type, file_name)
            if os.path.getsize(file_path) == 0:
                continue

            df = pd.read_csv(file_path, header=None)
            feature_len = df.shape[0]
            x_batch = []

            if feature_type == "Edge":
                for i in range(len_limit):
                    x_batch.append([0 for j in range(len_limit)])
                for index, row in df.iterrows():
                    if int(row[0]) < len_limit and int(row[1]) < len_limit:
                        x_batch[int(row[0])][int(row[1])] = 1
            else:
                for index, row in df.iterrows():
                    if index < len_limit:
                        x_batch.append(list(row))
                if feature_len < len_limit:
                    for i in range(feature_len, len_limit):
                        x_batch.append([0 for j in range(df.shape[1])])

            x.append(x_batch)
            y.append([1, 0] if label == "False" else [0, 1])
            num += 1

            if num == get_config("batch_size"):
                # print(np.array(x).shape)
                yield np.array(x), np.array(y)
                x.clear()
                y.clear()
                num = 0


def vector_generate(output_path, files, seq_len):
    return feature_generate(output_path, "WordVector", files, seq_len)


def graph_generate(output_path, feature_type, files, node_len):
    node_generator = feature_generate(output_path, feature_type, files, node_len)
    edge_generator = feature_generate(output_path, "Edge", files, node_len)
    while True:
        node_x, node_y = node_generator.__next__()
        edge_x, edge_y = edge_generator.__next__()
        yield [node_x, edge_x], node_y
