<template>
<!--  <div id="app">-->
<!--    <img src="./assets/logo.png">-->
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="上传文件" name="first">
        <el-upload
          class="upload-demo"
          ref="upload"
          action= "/upload/"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :on-success="handleSuccess"
          :file-list="fileList"
          :auto-upload="false">
          <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
          <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
          <div slot="tip" class="el-upload__tip">只能上传zip文件，且不超过500kb</div>
        </el-upload>
      </el-tab-pane>
      <el-tab-pane label="查看扫描结果" name="second">
        <el-table
          :data="tableData"
          style="width: 100%"
          max-height="400">
          <el-table-column
            fixed
            prop="id"
            label="漏洞ID"
            width="150">
          </el-table-column>
          <el-table-column
            prop="sourceFile"
            label="漏洞源文件"
            width="300">
          </el-table-column>
          <el-table-column
            prop="level"
            label="漏洞等级"
            width="120">
          </el-table-column>
          <el-table-column
            prop="methodName"
            label="漏洞方法"
            width="120">
          </el-table-column>
          <el-table-column
            prop="toolName"
            label="扫描工具"
            width="300">
          </el-table-column>
          <el-table-column
            prop="start"
            label="漏洞定位"
            width="120">
          </el-table-column>
          <el-table-column
            prop="bugType"
            label="漏洞类型"
            width="200">
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="danger"
                @click="handleSlice(scope.$index, scope.row)">查看切片结果</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="查看统计图表" name="third">
        <div class="box">
        <div id="left-box" style="width: 600px;height:400px;">
        </div>
        <div id="right-box" style="width: 600px;height:400px;">

        </div>
        </div>
<!--        <div>-->
<!--          <div id="main" style="width: 600px;height:400px;">-->
<!--          </div>-->
<!--        </div>-->
      </el-tab-pane>

      <el-tab-pane label="相似度检测" name="forth">
        <router-link to="/start">相似度检测</router-link>
      </el-tab-pane>
    </el-tabs>
<!--  </div>-->
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      fileList: [],
      activeName: 'first',
      filepath: '',
      tabletitle: '',
      dir_path: '',
      spot_path: '',
      find_path: '',
      projectname: '',
      tableData: [{
        date: '2016-05-03',
        name: '王小虎',
        province: '上海',
        city: '普陀区',
        address: '上海市普陀区金沙江路 1518 弄',
        zip: 200333
      }, {
        date: '2016-05-02',
        name: '王小虎',
        province: '上海',
        city: '普陀区',
        address: '上海市普陀区金沙江路 1518 弄',
        zip: 200333
      }, {
        date: '2016-05-04',
        name: '王小虎',
        province: '上海',
        city: '普陀区',
        address: '上海市普陀区金沙江路 1518 弄',
        zip: 200333
      }, {
        date: '2016-05-01',
        name: '王小虎',
        province: '上海',
        city: '普陀区',
        address: '上海市普陀区金沙江路 1518 弄',
        zip: 200333
      }, {
        date: '2016-05-08',
        name: '王小虎',
        province: '上海',
        city: '普陀区',
        address: '上海市普陀区金沙江路 1518 弄',
        zip: 200333
      }, {
        date: '2016-05-06',
        name: '王小虎',
        province: '上海',
        city: '普陀区',
        address: '上海市普陀区金沙江路 1518 弄',
        zip: 200333
      }, {
        date: '2016-05-07',
        name: '王小虎',
        province: '上海',
        city: '普陀区',
        address: '上海市普陀区金沙江路 1518 弄',
        zip: 200333
      }]
    };
  },

  methods: {
    handleClick(tab, event) {
      if (tab.name.toString().trim() == "first".trim()) {
        var url = '/autoscan';
        const loading = this.$loading({
          lock: true,//lock的修改符--默认是false
          text: '扫描漏洞中',//显示在加载图标下方的加载文案
          spinner: 'el-icon-loading',//自定义加载图标类名
          background: 'rgba(0, 0, 0, 0.7)',//遮罩层颜色
          target: document.querySelector('#table')//loadin覆盖的dom元素节点
        });
        this.$ajax.get(url, {
          params: {
            //需要替换
            jarpath: '/root/upload/CWE259_Hard_Coded_Password/CWE259_Hard_Coded_Password.jar'
          }
        }).then((res) => {
          // alert(JSON.stringify(res));
          this.spot_path = res.data.spot_path;
          this.find_path = res.data.find_path;
          var dealurl = '/dealdata';
          this.$ajax.get(dealurl, {
            params: {
              //需要替换
              spot_path: this.spot_path,
              find_path: this.find_path,
              dirpath: this.dir_path,
              jarpath: this.filepath
            }
          }).then((res) => {
            loading.close();

          });
          // alert(this.spot_path);
          // alert(this.find_path);
        });
      }
      if (tab.name.toString().trim() == "second".trim()) {
        var url = '/loaddata';
        this.$ajax.get(url, {
          params: {
            jarpath: '/root/upload/CWE259_Hard_Coded_Password/CWE259_Hard_Coded_Password.jar'
          }
        }).then((res) => {
          var table = res.data.data;
          this.tableData = table;
        });
      }
      if (tab.name.toString().trim() == "third".trim()) {
        var url = "/getdata";
        const loading = this.$loading({
          lock: true,//lock的修改符--默认是false
          text: '加载图表中',//显示在加载图标下方的加载文案
          spinner: 'el-icon-loading',//自定义加载图标类名
          background: 'rgba(0, 0, 0, 0.7)',//遮罩层颜色
          target: document.querySelector('#table')//loadin覆盖的dom元素节点
        });
        this.$ajax.get(url, {
          params: {
            jarpath: '/root/upload/CWE259_Hard_Coded_Password/CWE259_Hard_Coded_Password.jar'
          }
        }).then((res) => {
          //成功回调函数停止加载
            loading.close()
            var name = res.data.type;
            var count = res.data.count;
            var tabledata = [];
            for(let j = 0 ; j < name.length ; j ++) {
              var map = new Object();
              map.value = count[j];
              map.name = name[j];
              tabledata.push(map);
            }
            var option = {
              title:{
                text:"CWE259_Hard_Coded_Password" + "漏洞类别统计图",
                x:'center',
                y: 'bottom'
              },
              tooltip: {
                trigger: 'item',
                formatter: '{a} <br/>{b} : {c} ({d}%)'
              },
              legend: {
                orient: 'vertical',
                left: 'left',
                data: name
              },
              series: [
                {
                  name: 'Bug类型',
                  type: 'pie',
                  radius: '55%',
                  center: ['50%', '60%'],
                  data: tabledata,
                  emphasis: {
                    itemStyle: {
                      shadowBlur: 10,
                      shadowOffsetX: 0,
                      shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                  }
                }
              ]
            };
            let myChart = this.$echarts.init(document.getElementById('left-box'))
            myChart.setOption(option);
          });

          var option2  = {
            xAxis: {
              type: 'category',
              data: ['正报数量', '误报数量']
            },
            yAxis: {
              type: 'value'
            },
            series: [{
              data: [6, 75],
              type: 'bar',
              showBackground: true,
              backgroundStyle: {
                color: 'rgba(220, 220, 220, 0.8)'
              }
            }]
          };
        let myChart = this.$echarts.init(document.getElementById('right-box'))
        myChart.setOption(option2);
      }
      // console.log(tab, event);
    },

    submitUpload() {
      this.$refs.upload.submit();
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },

    handleSuccess(res, file, fileList) {
      this.filepath = res.msg.split("#")[0];
      this.projectname = res.msg.split("#")[1];
      this.dir_path = res.msg.split("#")[2];
      var url = '/autoscan';
      // this.$ajax.get(url, {
      //   params: {
      //     jarpath: '/root/upload/CWE259_Hard_Coded_Password/CWE259_Hard_Coded_Password.jar'
      //   }
      // }).then((res) => {
      //   alert(JSON.stringify(res));
      //   alert(res.data.spot_path);
      //   alert(res.data.find_path);
      //   this.spot_path = res.data.spot_path;
      //   this.find_path = res.data.find_path;
      //   alert(this.spot_path);
      //   alert(this.find_path);
      // });

    },

    deleteRow(index, rows) {
      rows.splice(index, 1);
    },

    handleSlice(index, row) {
      // alert(row.bugType);
      // alert(row.start);
      // alert(row.className);
      var url = '/showslice';
      this.$ajax.get(url, {
        params: {
          jarpath: this.filepath,
          methodname: row.methodName,
          classname:  row.className,
          linenumber: row.start,
          filepath: row.file_path
        }
      }).then((res) => {
        var data = res.data;
        var show = '';
        show = show + '<strong>这是 <i>扫描定位</i> 片段:</strong>';
        show = show + '<div>' + data[data.length - 1] + '</div>';
        show = show + '<strong>这是 <i>切片</i> 结果:</strong>';
        for (let i = 0 ; i < data.length; i ++) {
          show = show + '<div>' + data[i] + '</div>';
        }
        this.$alert(show, '内容详情', {
          confirmButtonText: '确定',
          dangerouslyUseHTMLString: true
        });
        console.log(res.data.length);
        console.log(res);
      });
      // console.log(index, row);
    },

  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

#left-box,#right-box{
  width: 50%;
  height: 100%;
  float: left;//设置两个盒子float:left
}
</style>
