def get_config(name):
    config = {
        "seq_len": 256,
        "node_len": 36,
        "batch_size": 8
    }
    return config[name]
