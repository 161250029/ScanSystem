<template>

    <el-tabs  v-model="activeName">

      <el-tab-pane label="查看扫描结果" name="first">
        <el-table
          :data="tableData"
          style="width: 100%"
          max-height="250">
          <el-table-column
            fixed
            prop="id"
            label="编号"
            width="150">
          </el-table-column>
          <el-table-column
            prop="size"
            label="漏洞数量"
            width="120">
          </el-table-column>
          <el-table-column
            prop="mode"
            label="常见类型"
            width="400">
          </el-table-column>
          <el-table-column
            label="操作"
            width="120">
            <template slot-scope="scope">
              <el-button
                @click.native.prevent="starttag(scope.$index, tableData)"
                type="text"
                size="small">
                开始标注
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="查看统计图表" name="second">
        <div>
          <div id="main" style="width: 800px;height:450px;">
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="众包工作界面" name="third">
        <div class="box">
          <div class="left-box">
            <el-divider></el-divider>
            <el-form ref="form" :model="form" label-width="200px">
              <el-form-item label="漏洞编号">
                <el-input v-model="form.name" :disabled="true" style="width:400px" align="left"></el-input>
              </el-form-item>
              <el-form-item label="威胁等级">
                <el-select v-model="form.region" placeholder="请选择威胁等级" style="width:400px" align="left">
                  <el-option label="微小" value="1"></el-option>
                  <el-option label="中等" value="2"></el-option>
                  <el-option label="严重" value="3"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="漏洞类型">
                <!--                <el-select v-model="form.delivery" placeholder="请选择漏洞类型" style="width:400px" align="left">-->
                <!--                  <el-option label="类型1" value="1"></el-option>-->
                <!--                  <el-option label="类型2" value="2"></el-option>-->
                <!--                  <el-option label="类型3" value="3"></el-option>-->
                <!--                </el-select>-->
                <el-cascader :options="type"  v-model="form.delivery" ref="cascaderAddr" style="width:400px" align="left" :show-all-levels="false"></el-cascader>
              </el-form-item>

              <el-form-item label="相关备注">
                <el-input type="textarea" v-model="form.desc"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="onSubmit">提交</el-button>
                <!--                <el-button @click="drawer = true" type="primary" style="margin-left: 16px;">-->
                <!--                  查看源码-->
                <!--                </el-button>-->
              </el-form-item>
            </el-form>
          </div>
          <div class="right-box">
            <el-input
              type="textarea"
              :rows="15"
              :disabled="true"
              placeholder=""
              v-model="codearea">
            </el-input>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>



</template>

<script>
  import qs from 'qs'
  export default {
    name: 'App',
    // data() {
    //   return {
    //
    //   }
    // },
    // methods: {
    //   onSubmit() {
    //     console.log('submit!');
    //   }
    // },
    data() {
      return {
        activeName: 'first',
        tableData: [
          {
            id: '2016-05-03',
            size: '王小虎',
            mode: '上海'
          }
        ],
        form: {
          name: 'test',
          region: '',
          delivery: '',
          desc: ''
        },
        drawer: false,
        codearea: '',
        curbug: {
          id:'',
          path: ''
        },
        curindex: 0,
        bugs:[
        ],
        type:[{
          value: 'daohang',
          label: 'Security',
          children: [{
            value: 'cexiangdaohang',
            label: 'QBA_QUESTIONABLE_BOOLEAN_ASSIGNMENT'
          }, {
            value: 'dingbudaohang',
            label: 'RV_REM_OF_RANDOM_INT'
          },{
            value: 'cexiangdaohang',
            label: 'RANGE_ARRAY_INDEX'
          }, {
            value: 'dingbudaohang',
            label: 'DM_STRING_CTOR'
          },{
            value: 'cexiangdaohang',
            label: 'RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE'
          }, {
            value: 'dingbudaohang',
            label: 'PT_ABSOLUTE_PATH_TRAVERSAL'
          },{
            value: 'cexiangdaohang',
            label: 'NP_NULL_ON_SOME_PATH_EXCEPTION'
          }, {
            value: 'dingbudaohang',
            label: 'ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD'
          },{
            value: 'cexiangdaohang',
            label: 'SIC_INNER_SHOULD_BE_STATIC_ANON'
          }, {
            value: 'dingbudaohang',
            label: 'HRS_REQUEST_PARAMETER_TO_HTTP_HEADER'
          }]
        },{
          value: 'daohang',
          label: 'Experimental',
          children: [{
            value: 'cexiangdaohang',
            label: 'FI_EXPLICIT_INVOCATION'
          }, {
            value: 'dingbudaohang',
            label: 'XSS_SERVLET'
          },{
            value: 'cexiangdaohang',
            label: 'DMI_CONSTANT_DB_PASSWORD'
          }, {
            value: 'dingbudaohang',
            label: 'INT_BAD_COMPARISON_WITH_INT_VALUE'
          },{
            value: 'cexiangdaohang',
            label: 'LDAP_INJECTION'
          }, {
            value: 'dingbudaohang',
            label: 'RR_NOT_CHECKED'
          },{
            value: 'cexiangdaohang',
            label: 'UNSAFE_HASH_EQUALS'
          }, {
            value: 'dingbudaohang',
            label: 'OBJECT_DESERIALIZATION'
          },{
            value: 'cexiangdaohang',
            label: 'PADDING_ORACLE'
          }, {
            value: 'dingbudaohang',
            label: 'OS_OPEN_STREAM_EXCEPTION_PATH'
          }]
        },{
          value: 'daohang',
          label: 'Bad practice',
          children: [{
            value: 'cexiangdaohang',
            label: 'NP_GUARANTEED_DEREF'
          }, {
            value: 'dingbudaohang',
            label: 'SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING'
          },{
            value: 'cexiangdaohang',
            label: 'IL_INFINITE_RECURSIVE_LOOP'
          }, {
            value: 'dingbudaohang',
            label: 'UC_USELESS_OBJECT'
          },{
            value: 'cexiangdaohang',
            label: 'UC_USELESS_CONDITION_TYPE'
          }, {
            value: 'dingbudaohang',
            label: 'RCN_REDUNDANT_NULLCHECK_OF_NULL_VALUE'
          },{
            value: 'cexiangdaohang',
            label: 'INT_VACUOUS_COMPARISON'
          }, {
            value: 'dingbudaohang',
            label: 'BC_UNCONFIRMED_CAST_OF_RETURN_VALUE'
          },{
            value: 'cexiangdaohang',
            label: 'PATH_TRAVERSAL_IN'
          }, {
            value: 'dingbudaohang',
            label: 'UL_UNRELEASED_LOCK_EXCEPTION_PATH'
          }]
        },{
          value: 'daohang',
          label: 'Correctness',
          children: [{
            value: 'cexiangdaohang',
            label: 'UPM_UNCALLED_PRIVATE_METHOD'
          }, {
            value: 'dingbudaohang',
            label: 'HARD_CODE_PASSWORD'
          },{
            value: 'cexiangdaohang',
            label: 'DMI_INVOKING_TOSTRING_ON_ARRAY'
          }, {
            value: 'dingbudaohang',
            label: 'ECB_MODE'
          },{
            value: 'cexiangdaohang',
            label: 'ES_COMPARING_STRINGS_WITH_EQ'
          }, {
            value: 'dingbudaohang',
            label: 'NP_ALWAYS_NULL'
          },{
            value: 'cexiangdaohang',
            label: 'SWL_SLEEP_WITH_LOCK_HELD'
          }, {
            value: 'dingbudaohang',
            label: 'DB_DUPLICATE_BRANCHES'
          },{
            value: 'cexiangdaohang',
            label: 'DMI_HARDCODED_ABSOLUTE_FILENAME'
          }, {
            value: 'dingbudaohang',
            label: 'ODR_OPEN_DATABASE_RESOURCE'
          }]
        }]

      }
    },
    mounted(){   //页面初始化方法
      var staticsurl = "/getstatics";
      this.$ajax.get(staticsurl).then((res) => {
        var items = res.data;

        var data1 = new Array();
        for(var i=0; i < items.length; i++){
          data1[i] = {value:items[i].num,name:items[i].typename}
        }

        // var tabledata = [];
        // for(let j = 0 ; j < items.length ; j ++) {
        //   var map = new Object();
        //   map.typename = items[j].typename;
        //   map.num = items[j].num;
        //   tabledata.push(map);
        // }
        var option = {
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
          },
          legend: {
            orient: 'vertical',
            left: 'left',
            y:'bottom',
            data: data1
          },
          series: [
            {
              name: 'Bug类型',
              type: 'pie',
              radius: '55%',
              center: ['80%', '40%'],
              data: data1,
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
        let myChart = this.$echarts.init(document.getElementById('main'))
        myChart.setOption(option);
      });

      var groupurl = '/getgroups';
      this.$ajax.get(groupurl).then((res) => {
        var table = res.data;
        this.tableData = table;
      });

    },
    methods: {
      starttag(index, rows) {
        console.log("test button"+ index);
        var bugsurl = '/getbugs';

        this.$ajax.get(bugsurl, {
          params: {
            id: index
          }
        }).then((res) => {
          var thisbugs = res.data;
          this.bugs = thisbugs;
          this.curindex = 0;
          this.setNewBug(thisbugs, this.curindex);
          this.activeName = 'third';
        });
      },
      setNewBug(bugs, curindex) {
        console.log('setnewbug!');
        if(curindex == bugs.length){
          this.activeName= 'first';
          return;
        }
        this.curbug = this.bugs[curindex];
        var curid = this.curbug.id;
        this.form.name = curid;
        var n = this.curbug.code.replace(/\\/g,"\/").split("Desktop");
        // alert(n);

        var path = "/root"+n[1];
        var path = this.curbug.code;
        var codeurl= "/getcode";
        this.$ajax.get(codeurl, {
          params: {
            id: curid,
            path: path
          }
        }).then((coderes) => {
          var codetext = coderes.data;
          this.codearea = codetext;
        });
        this.form.desc = '';
        this.form.delivery='';
        this.form.region = '';

      },
      onSubmit() {
        console.log('submit!');
        var correctid = this.curbug.id;
        let [llable,lvalue] = this.form.region.split('|');
        var correctlevel = lvalue;
        // let [tlable,tvalue] = this.form.delivery.split('|');
        var thsAreaCode = this.$refs['cascaderAddr'].getCheckedNodes()[0].label;
        alert(thsAreaCode);
        // var typelength = thsAreaCode.length;
        var correcttype = thsAreaCode;
        var correctcomment = this.form.desc;

        var submiturl= "/submitbug";
        this.$ajax.get(submiturl, {
          params: {
            id: correctid,
            type: correcttype,
            level: correctlevel,
            comments: correctcomment
          }
        }).then((res) => {
          console.log("success submit");
          console.log("comment: "+correctcomment);
          this.curindex = this.curindex+1;
          this.setNewBug(this.bugs, this.curindex);
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

  .box{
    /*width: 400px;*/
    /*height: 300px;*/
  }
  .left-box,.right-box{
    width: 50%;
    height: 100%;
    float: left;//设置两个盒子float:left
  }
  .left-box{
  }
  .right-box{
  }
</style>
