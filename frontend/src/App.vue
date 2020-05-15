<template>
  <div id="app">
<!--    <img src="./assets/logo.png">-->
    <router-view/>
  </div>
</template>

<!--<script>-->
<!--export default {-->
<!--  name: 'App',-->
<!--  data() {-->
<!--    return {-->
<!--      fileList: [],-->
<!--      activeName: 'first',-->
<!--      filepath: '',-->
<!--      tabletitle: '',-->
<!--      dir_path: '',-->
<!--      spot_path: '',-->
<!--      find_path: '',-->
<!--      projectname: '',-->
<!--      tableData: [{-->
<!--        date: '2016-05-03',-->
<!--        name: '王小虎',-->
<!--        province: '上海',-->
<!--        city: '普陀区',-->
<!--        address: '上海市普陀区金沙江路 1518 弄',-->
<!--        zip: 200333-->
<!--      }, {-->
<!--        date: '2016-05-02',-->
<!--        name: '王小虎',-->
<!--        province: '上海',-->
<!--        city: '普陀区',-->
<!--        address: '上海市普陀区金沙江路 1518 弄',-->
<!--        zip: 200333-->
<!--      }, {-->
<!--        date: '2016-05-04',-->
<!--        name: '王小虎',-->
<!--        province: '上海',-->
<!--        city: '普陀区',-->
<!--        address: '上海市普陀区金沙江路 1518 弄',-->
<!--        zip: 200333-->
<!--      }, {-->
<!--        date: '2016-05-01',-->
<!--        name: '王小虎',-->
<!--        province: '上海',-->
<!--        city: '普陀区',-->
<!--        address: '上海市普陀区金沙江路 1518 弄',-->
<!--        zip: 200333-->
<!--      }, {-->
<!--        date: '2016-05-08',-->
<!--        name: '王小虎',-->
<!--        province: '上海',-->
<!--        city: '普陀区',-->
<!--        address: '上海市普陀区金沙江路 1518 弄',-->
<!--        zip: 200333-->
<!--      }, {-->
<!--        date: '2016-05-06',-->
<!--        name: '王小虎',-->
<!--        province: '上海',-->
<!--        city: '普陀区',-->
<!--        address: '上海市普陀区金沙江路 1518 弄',-->
<!--        zip: 200333-->
<!--      }, {-->
<!--        date: '2016-05-07',-->
<!--        name: '王小虎',-->
<!--        province: '上海',-->
<!--        city: '普陀区',-->
<!--        address: '上海市普陀区金沙江路 1518 弄',-->
<!--        zip: 200333-->
<!--      }]-->
<!--    };-->
<!--  },-->

<!--  methods: {-->
<!--    handleClick(tab, event) {-->
<!--      if (tab.name.toString().trim() == "first".trim()) {-->
<!--        var url = '/autoscan';-->
<!--        this.$ajax.get(url, {-->
<!--          params: {-->
<!--            //需要替换-->
<!--            jarpath: '/root/upload/CWE259_Hard_Coded_Password/CWE259_Hard_Coded_Password.jar'-->
<!--          }-->
<!--        }).then((res) => {-->
<!--          alert(JSON.stringify(res));-->
<!--          this.spot_path = res.data.spot_path;-->
<!--          this.find_path = res.data.find_path;-->
<!--          var dealurl = '/dealdata';-->
<!--          this.$ajax.get(dealurl, {-->
<!--            params: {-->
<!--              //需要替换-->
<!--              spot_path: this.spot_path,-->
<!--              find_path: this.find_path,-->
<!--              dirpath: this.dir_path,-->
<!--              jarpath: this.filepath-->
<!--            }-->
<!--          }).then((res) => {-->

<!--          });-->
<!--          // alert(this.spot_path);-->
<!--          // alert(this.find_path);-->
<!--        });-->
<!--      }-->
<!--      if (tab.name.toString().trim() == "second".trim()) {-->
<!--        var url = '/loaddata';-->
<!--        this.$ajax.get(url, {-->
<!--          params: {-->
<!--            jarpath: '/root/upload/CWE259_Hard_Coded_Password/CWE259_Hard_Coded_Password.jar'-->
<!--          }-->
<!--        }).then((res) => {-->
<!--          var table = res.data.data;-->
<!--          this.tableData = table;-->
<!--        });-->
<!--      }-->
<!--      if (tab.name.toString().trim() == "third".trim()) {-->
<!--        var url = "/getdata";-->
<!--        this.$ajax.get(url, {-->
<!--          params: {-->
<!--            jarpath: '/root/upload/CWE259_Hard_Coded_Password/CWE259_Hard_Coded_Password.jar'-->
<!--          }-->
<!--        }).then((res) => {-->
<!--            var name = res.data.type;-->
<!--            var count = res.data.count;-->
<!--            var tabledata = [];-->
<!--            for(let j = 0 ; j < name.length ; j ++) {-->
<!--              var map = new Object();-->
<!--              map.value = count[j];-->
<!--              map.name = name[j];-->
<!--              tabledata.push(map);-->
<!--            }-->
<!--            var option = {-->
<!--              title:{-->
<!--                text:"CWE259_Hard_Coded_Password" + "漏洞类别统计图",-->
<!--                x:'center',-->
<!--                y: 'bottom'-->
<!--              },-->
<!--              tooltip: {-->
<!--                trigger: 'item',-->
<!--                formatter: '{a} <br/>{b} : {c} ({d}%)'-->
<!--              },-->
<!--              legend: {-->
<!--                orient: 'vertical',-->
<!--                left: 'left',-->
<!--                data: name-->
<!--              },-->
<!--              series: [-->
<!--                {-->
<!--                  name: 'Bug类型',-->
<!--                  type: 'pie',-->
<!--                  radius: '55%',-->
<!--                  center: ['50%', '60%'],-->
<!--                  data: tabledata,-->
<!--                  emphasis: {-->
<!--                    itemStyle: {-->
<!--                      shadowBlur: 10,-->
<!--                      shadowOffsetX: 0,-->
<!--                      shadowColor: 'rgba(0, 0, 0, 0.5)'-->
<!--                    }-->
<!--                  }-->
<!--                }-->
<!--              ]-->
<!--            };-->
<!--            let myChart = this.$echarts.init(document.getElementById('main'))-->
<!--            myChart.setOption(option);-->
<!--          });-->
<!--      }-->
<!--      // console.log(tab, event);-->
<!--    },-->

<!--    submitUpload() {-->
<!--      this.$refs.upload.submit();-->
<!--    },-->
<!--    handleRemove(file, fileList) {-->
<!--      console.log(file, fileList);-->
<!--    },-->
<!--    handlePreview(file) {-->
<!--      console.log(file);-->
<!--    },-->

<!--    handleSuccess(res, file, fileList) {-->
<!--      this.filepath = res.msg.split("#")[0];-->
<!--      this.projectname = res.msg.split("#")[1];-->
<!--      this.dir_path = res.msg.split("#")[2];-->
<!--      var url = '/autoscan';-->
<!--      // this.$ajax.get(url, {-->
<!--      //   params: {-->
<!--      //     jarpath: '/root/upload/CWE259_Hard_Coded_Password/CWE259_Hard_Coded_Password.jar'-->
<!--      //   }-->
<!--      // }).then((res) => {-->
<!--      //   alert(JSON.stringify(res));-->
<!--      //   alert(res.data.spot_path);-->
<!--      //   alert(res.data.find_path);-->
<!--      //   this.spot_path = res.data.spot_path;-->
<!--      //   this.find_path = res.data.find_path;-->
<!--      //   alert(this.spot_path);-->
<!--      //   alert(this.find_path);-->
<!--      // });-->

<!--    },-->

<!--    deleteRow(index, rows) {-->
<!--      rows.splice(index, 1);-->
<!--    },-->

<!--    handleSlice(index, row) {-->
<!--      // alert(row.bugType);-->
<!--      // alert(row.start);-->
<!--      // alert(row.className);-->
<!--      var url = '/showslice';-->
<!--      this.$ajax.get(url, {-->
<!--        params: {-->
<!--          jarpath: this.filepath,-->
<!--          methodname: row.methodName,-->
<!--          classname:  row.className,-->
<!--          linenumber: row.start,-->
<!--          filepath: row.file_path-->
<!--        }-->
<!--      }).then((res) => {-->
<!--        var data = res.data;-->
<!--        var show = '';-->
<!--        show = show + '<strong>这是 <i>扫描定位</i> 片段:</strong>';-->
<!--        show = show + '<div>' + data[data.length - 1] + '</div>';-->
<!--        show = show + '<strong>这是 <i>切片</i> 结果:</strong>';-->
<!--        for (let i = 0 ; i < data.length; i ++) {-->
<!--          show = show + '<div>' + data[i] + '</div>';-->
<!--        }-->
<!--        this.$alert(show, '内容详情', {-->
<!--          confirmButtonText: '确定',-->
<!--          dangerouslyUseHTMLString: true-->
<!--        });-->
<!--        console.log(res.data.length);-->
<!--        console.log(res);-->
<!--      });-->
<!--      // console.log(index, row);-->
<!--    },-->

<!--  }-->
<!--}-->
<!--</script>-->

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
