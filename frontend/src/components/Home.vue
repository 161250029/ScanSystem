<template>
<!--  <div id="app">-->
<!--    <img src="./assets/logo.png">-->
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="上传文件" name="first">

      <el-form ref="form" :model="form" label-width="80px" width="50%">
        <el-form-item label="上传源码:" prop="fileList">
          <el-upload
            class="upload-demo"
            ref="upload"
            action= "/upload/"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :on-success="handleSuccess"
            :file-list="fileList"
            :auto-upload="false"
          >
            <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
            <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
            <div slot="tip" class="el-upload__tip">只能上传zip文件，且不超过500kb</div>
          </el-upload>
        </el-form-item>

        <el-form-item label="项目名:" prop="name">
          <el-col :span="12">
            <el-input v-model="form.name" width="50%"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="项目描述:" prop="desc">
          <el-col :span="12">
            <el-input type="textarea" v-model="form.desc"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="onSubmit">提交测试</el-button>
          <el-button>取消</el-button>
        </el-form-item>
      </el-form>
      </el-tab-pane>

      <el-tab-pane label="任务审核" name="forth">
        <el-table
          :data="projectData"
          style="width: 100%"
          max-height="400">
          <el-table-column
            fixed
            prop="date"
            label="提测时间"
            width="250">
          </el-table-column>
          <el-table-column
            prop="projectname"
            label="任务名"
            width="200">
          </el-table-column>
          <el-table-column
            prop="projectjarpath"
            label="扫描文件"
            width="200">
          </el-table-column>
          <el-table-column
            prop="progress"
            label="测试进度"
            width="200">
            <template slot-scope="scope">
              <span v-if="scope.row.progress === 'doing' " style="color: #909399">进行中</span>
              <span v-else style="color: #67C23A">已完成</span>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="danger"
                @click="selectreport(scope.$index, scope.row)">查看报告</el-button>
              <router-link to="/start">
                <el-button
                  size="mini"
                  type="danger"
                >发布众审</el-button>
              </router-link>

              <router-link to="/codeFeature?/root/testcode">
                <el-button
                  size="mini"
                  type="danger"
                  >预测分析</el-button>
              </router-link>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="查看扫描结果" name="second">
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="grid-content bg-purple">
              <el-table
                :data="tableData"
                style="width: 100%"
                max-height="400">
                <el-table-column
                  fixed
                  prop="id"
                  label="漏洞ID"
                  width="100">
                </el-table-column>
                <el-table-column
                  prop="level"
                  label="漏洞等级"
                  width="100">
                </el-table-column>
                <el-table-column
                  prop="methodName"
                  label="漏洞方法"
                  width="100">
                </el-table-column>
                <el-table-column
                  prop="bugType"
                  label="漏洞类型"
                  width="150">
                </el-table-column>
                <el-table-column label="漏洞详情">
                  <template slot-scope="scope">
                    <el-button
                      size="mini"
                      type="danger"
                      @click="handleSlice(scope.$index, scope.row)">详情查看</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-col>

          <el-col :span="12">
            <div class="grid-content bg-purple">
              <el-card class="box-card" id = "card" hidden=true>
                <div slot="header" class="clearfix">
                  <span>漏洞详情</span>
                </div>
                <div id="tablecontent">
                  <div v-for="o in 4" :key="o" class="text item">
                    {{'列表内容 ' + o }}
                  </div>
                </div>
              </el-card>
            </div>
          </el-col>
        </el-row>


<!--        <div class="box">-->
<!--          <div id="left-block" style="width: 600px;height:600px;">-->
<!--          </div>-->
<!--          <div id="right-block" style="width: 600px;height:600px;">-->
<!--          </div>-->
<!--        </div>-->
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
    </el-tabs>
<!--  </div>-->
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      form: {
        name: '',
        desc: ''
      },
      fileList: [],
      activeName: 'first',
      filepath: '',
      tabletitle: '',
      dir_path: '',
      spot_path: '',
      find_path: '',
      projectname: '',
      tableData: [{
        id:'1',
        level:2,
        methodName:'bad',
        bugType:'xx'
      }

      ],
      projectData: [],
      bugcontent: ''

    };
  },

  methods: {
    handleClick(tab, event) {
      if (tab.name.toString().trim() == "first".trim()) {
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

        //后端提供一个危险等级的接口
        url = "/getleveldata"
        this.$ajax.get(url, {
          params: {
            jarpath: '/root/upload/CWE259_Hard_Coded_Password/CWE259_Hard_Coded_Password.jar'
          }
        }).then((res) => {
          var name = ['高危','中危','低危'];
          var count = res.data;
          var tabledata = [];
          for(let j = 0 ; j < name.length ; j ++) {
            var map = new Object();
            map.value = count[j];
            map.name = name[j];
            tabledata.push(map);
          }
          var option = {
            title:{
              text:"CWE259_Hard_Coded_Password" + "漏洞等级统计图",
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
                name: '威胁等级',
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
          let myChart = this.$echarts.init(document.getElementById('right-box'))
          myChart.setOption(option);
        });


      }

      if (tab.name.toString().trim() == "forth".trim()) {
         //调用后端查询所有的任务
        var url = '/getProjects';
        this.$ajax.get(url).then((res) => {
             this.projectData = res.data;
        });
      }
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
    },

    onSubmit() {
      //存储任务，并设置任务为进行中
      var projectname = this.form.name;
      var projectdesc = this.form.desc;
      this.fileList = [];
      this.$refs["form"].resetFields();
      var url = '/saveproject';
      this.$ajax.get(url, {
        params: {
          //需要替换
          projectname: projectname,
          projectdesc: projectdesc,
          filepath: this.filepath
        }
      }).then((res) => {
        this.$refs["form"].resetFields();
        this.$message({
          message: '恭喜你，发布任务成功',
          type: 'success'
        });
      });

      var url = '/autoscan';
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
          // loading.close();
          //这里要更新任务审核进度信息
          var updateurl = '/updateproject';
          this.$ajax.get(updateurl, {
            params: {
              projectname: projectname
            }
          }).then((res) => {
            this.projectData = res.data;
          });

        });
      });

    },

    deleteRow(index, rows) {
      rows.splice(index, 1);
    },

    handleSlice(index, row) {
      var content = "" +
        "<div  class=\"text item\">\n" +
        "                    漏洞文件： " + row.sourceFile + "\n" +
        "                  </div>\n" +
        "<div  class=\"text item\">\n" +
        "                    使用扫描工具： " + row.toolName +"\n" +
        "                  </div>\n" +
        "<div  class=\"text item\">\n" +
        "                    漏洞方法名： " + row.methodName +"\n" +
        "                  </div>\n" +
        "<div  class=\"text item\">\n" +
        "                    漏洞扫描定位： " + row.start +"\n" +
        "                  </div>\n";
      // document.getElementById("tablecontent").innerHTML = content;
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
        content = content + "<div  class=\"text item\">\n" +
        "                    切片结果： \n";
        var show = '';
        show = show + '<strong>这是 <i>扫描定位</i> 片段:</strong>';
        show = show + '<div>' + data[data.length - 1] + '</div>';
        show = show + '<strong>这是 <i>切片</i> 结果:</strong>';
        for (let i = 0 ; i < data.length; i ++) {
          show = show + '<div>' + data[i] + '</div>';
          content = content + '<div>' + data[i] + '</div>\n';
        }
        content = content  + "                  </div>\n";
        this.$alert(show, '内容详情', {
          confirmButtonText: '确定',
          dangerouslyUseHTMLString: true
        });
        console.log(res.data.length);
        console.log(res);
        document.getElementById("card").removeAttribute("hidden");
        document.getElementById("tablecontent").innerHTML = content;
      });
    },

    selectreport(index, row) {
      //这个只需要把jarpath改成实参就可了
      this.activeName = 'second';
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
