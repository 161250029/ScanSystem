def get_config(name):
    config = {
        "seq_len": 256,
        "node_len": 32,
        "batch_size": 8
    }
    return config[name]
