<template>
  <el-tabs  v-model="activeName">
        <el-tab-pane label="众包任务列表" name="tasklist">
          <el-table
            :data="taskData"
            max-height="500">
            <el-table-column
              fixed
              prop="taskid"
              label="聚类类簇编号">
            </el-table-column>
            <el-table-column
              prop="tasksize"
              label="采样类簇数量">
            </el-table-column>
            <el-table-column
              prop="taskprocess"
              label="已标注数量">
            </el-table-column>
            <el-table-column prop="taskstate" label="采样类簇状态">
              <template scope="scope">
                <span v-if="scope.row.taskprocess === 0" style="color: #909399">未进行</span>
                <span v-else-if="scope.row.tasksize === scope.row.taskprocess" style="color: #67C23A">已完成</span>
                <span v-else style="color: #f18603">进行中</span>
              </template>
            </el-table-column>
            <el-table-column
              label="操作">
              <template slot-scope="scope">
                <el-button
                  @click.native.prevent="tagtask(scope.row.taskid, taskData)"
                  type="text"
                  size="small">
                  开始标注
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="漏洞审核界面" name="buglist">
          <div>
            <div class="left-side">
              <el-table
                :data="tableDataEnd"
                max-height="500">
                <el-table-column
                  fixed
                  prop="bugid"
                  label="漏洞编号">
                </el-table-column>
                <el-table-column prop="bugstate" label="审核状态">
                  <template scope="scope">
                    <span v-if="scope.row.bugstate==='已审核'" style="color: #4fde4f">已审核</span>
                    <span v-else style="color: #ff0084">待审核</span>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="bugdetails"
                  label="漏洞详情">
                  <template slot-scope="scope">
                    <el-button
                      @click.native.prevent="getdetails(scope.row.bugid)"
                      type="text"
                      size="small">
                      漏洞详情
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
              <div class="pagination">
                <el-pagination
                  @size-change="handleSizeChange"
                  @current-change="handleCurrentChange"
                  :current-page="currentPage"
                  :page-sizes="[5, 10, 20, 50]"
                  :page-size="pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="totalItems">
                </el-pagination>
              </div>
            </div>
            <div class="right-side">
              <el-card class="box-card">
                <div slot="header" class="clearfix">
                  <span>类簇采样信息</span>
                </div>
                <div id="clusterId" class="text item" style="text-align: left">
                  聚类类簇ID：1
                </div>
                <div id="clusterSize" class="text item" style="text-align: left">
                  聚类采样数量：12
                </div>
                <div id="clusterProcess" class="text item" style="text-align: left">
                  采样标注进度：4
                </div>
                <div id="clusterState" class="text item" style="text-align: left">
                  类簇标注状态：进行中
                </div>
              </el-card>
            </div>
          </div>

        </el-tab-pane>
        <el-tab-pane label="漏洞详细信息" name="bugdetail">
          <div class="box">
            <div class="left-box">
              <el-divider style="font-size: 40px">漏洞信息</el-divider>
              <el-form ref="form" :model="bugdetail" label-width="200px">
                <el-form-item label="漏洞编号">
                  <el-input v-model="bugdetail.id" readonly="true" style="width:400px" align="left"></el-input>
                </el-form-item>
                <el-form-item label="威胁等级">
                  <el-select v-model="bugdetail.level" ref="itemSelect" placeholder="请选择威胁等级" style="width:400px" align="left">
                    <el-option
                      v-for="item in tlevel"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                    </el-option>
                  </el-select>
  <!--                <el-input v-model="bugdetail.level" readonly="true" style="width:400px" align="left"></el-input>-->
                </el-form-item>
                <el-form-item label="漏洞类型">

                  <el-cascader :options="type"  v-model="bugdetail.type" ref="cascaderType" style="width:400px" align="left" :show-all-levels="false"></el-cascader>
  <!--                <el-input v-model="bugdetail.type" readonly="true" style="width:400px" align="left"></el-input>-->
                </el-form-item>
                <el-form-item label="相关备注">
                  <el-input type="textarea"  v-model="bugdetail.comments" style="width:400px" :rows="4"></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="success" @click="confirmpass()">审核通过</el-button>
                  <el-button type="warning" @click="confirmfalse()">标记误报</el-button>
                </el-form-item>
              </el-form>
            </div>
            <div class="right-box">
              <el-divider style="font-size: 40px">相关源码</el-divider>
              <el-collapse v-model="activecollapse" @change="handleChange">
                <el-collapse-item title="漏洞对应切片" name="1">
                  <el-input
                    type="textarea"
                    :rows="13"
                    readonly="true"
                    placeholder=""
                    v-model="codedetail">
                  </el-input>
                </el-collapse-item>
                <el-collapse-item title="漏洞对应函数" name="2">
                  <el-input
                    type="textarea"
                    readonly="true"
                    placeholder=""
                    v-model="slicedetail">
                  </el-input>
                </el-collapse-item>
              </el-collapse>

            </div>
          </div>
        </el-tab-pane>
  </el-tabs>
</template>


<script>
  import qs from 'qs'
export default {
  name: 'App',
  data() {
    return {
      activecollapse: [],
      tableDataEnd: [],
      currentPage: 1,
      pageSize: 5,
      totalItems: 0,
      filterTableDataEnd:[],

      activeName: 'tasklist',
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
      bugdetail: {
        id: '36451',
        level: '',
        type: '',
        comments: ''
      },
      codedetail: '',
      slicedetail: '',
      drawer: false,
      codearea: '',
      curbug: {
        id:'',
        path: ''
      },
      curindex: 0,
      bugs:[

      ],
      type:[
        {
        value: 'daohang',
        label: 'Security',
        children: [
          {
          value: 'EXTERNAL_CONFIG_CONTROL',
          label: 'EXTERNAL_CONFIG_CONTROL'
        },{
            value: 'DB_DUPLICATE_BRANCHES',
            label: 'DB_DUPLICATE_BRANCHES'
        },{
          value: 'ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD',
          label: 'ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD'
        }, {
          value: 'OBJECT_DESERIALIZATION',
          label: 'OBJECT_DESERIALIZATION'
        },{
          value: 'DMI_HARDCODED_ABSOLUTE_FILENAME',
          label: 'DMI_HARDCODED_ABSOLUTE_FILENAME'
        }, {
          value: 'ECB_MODE',
          label: 'ECB_MODE'
        },{
          value: 'HARD_CODE_KEY',
          label: 'HARD_CODE_KEY'
        },{
          value: 'SIC_INNER_SHOULD_BE_STATIC_ANON',
          label: 'SIC_INNER_SHOULD_BE_STATIC_ANON'
        }, {
          value: 'HRS_REQUEST_PARAMETER_TO_HTTP_HEADER',
          label: 'HRS_REQUEST_PARAMETER_TO_HTTP_HEADER'
        }]
      },
        {
        value: 'daohang2',
        label: 'Experimental',
        children: [{
          value: 'XSS_SERVLET',
          label: 'XSS_SERVLET'
        },{
          value: 'DMI_CONSTANT_DB_PASSWORD',
          label: 'DMI_CONSTANT_DB_PASSWORD'
        }, {
          value: 'INT_BAD_COMPARISON_WITH_INT_VALUE',
          label: 'INT_BAD_COMPARISON_WITH_INT_VALUE'
        },{
          value: 'LDAP_INJECTION',
          label: 'LDAP_INJECTION'
        }, {
          value: 'RR_NOT_CHECKED',
          label: 'RR_NOT_CHECKED'
        },{
          value: 'UNSAFE_HASH_EQUALS',
          label: 'UNSAFE_HASH_EQUALS'
        }, {
          value: 'PADDING_ORACLE',
          label: 'PADDING_ORACLE'
        }, {
          value: 'OS_OPEN_STREAM_EXCEPTION_PATH',
          label: 'OS_OPEN_STREAM_EXCEPTION_PATH'
        }]
      },
        {
        value: 'daohang3',
        label: 'Bad practice',
        children: [{
          value: 'NP_GUARANTEED_DEREF',
          label: 'NP_GUARANTEED_DEREF'
        }, {
          value: 'SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING',
          label: 'SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING'
        },{
          value: 'IL_INFINITE_RECURSIVE_LOOP',
          label: 'IL_INFINITE_RECURSIVE_LOOP'
        }, {
          value: 'UC_USELESS_OBJECT',
          label: 'UC_USELESS_OBJECT'
        },{
          value: 'UC_USELESS_CONDITION_TYPE',
          label: 'UC_USELESS_CONDITION_TYPE'
        }, {
          value: 'RCN_REDUNDANT_NULLCHECK_OF_NULL_VALUE',
          label: 'RCN_REDUNDANT_NULLCHECK_OF_NULL_VALUE'
        },{
          value: 'INT_VACUOUS_COMPARISON',
          label: 'INT_VACUOUS_COMPARISON'
        }, {
          value: 'BC_UNCONFIRMED_CAST_OF_RETURN_VALUE',
          label: 'BC_UNCONFIRMED_CAST_OF_RETURN_VALUE'
        },{
          value: 'PATH_TRAVERSAL_IN',
          label: 'PATH_TRAVERSAL_IN'
        }, {
          value: 'UL_UNRELEASED_LOCK_EXCEPTION_PATH',
          label: 'UL_UNRELEASED_LOCK_EXCEPTION_PATH'
        }]
      },{
        value: 'daohang4',
        label: 'Correctness',
        children: [{
          value: 'UPM_UNCALLED_PRIVATE_METHOD',
          label: 'UPM_UNCALLED_PRIVATE_METHOD'
        }, {
          value: 'HARD_CODE_PASSWORD',
          label: 'HARD_CODE_PASSWORD'
        },{
          value: 'DMI_INVOKING_TOSTRING_ON_ARRAY',
          label: 'DMI_INVOKING_TOSTRING_ON_ARRAY'
        },{
          value: 'ES_COMPARING_STRINGS_WITH_EQ',
          label: 'ES_COMPARING_STRINGS_WITH_EQ'
        }, {
          value: 'NP_ALWAYS_NULL',
          label: 'NP_ALWAYS_NULL'
        },{
          value: 'SWL_SLEEP_WITH_LOCK_HELD',
          label: 'SWL_SLEEP_WITH_LOCK_HELD'
        }, {
          value: 'DB_DUPLICATE_BRANCHES',
          label: 'DB_DUPLICATE_BRANCHES'
        },{
          value: 'ODR_OPEN_DATABASE_RESOURCE',
          label: 'ODR_OPEN_DATABASE_RESOURCE'
        }]
      }],
      tlevel:[
        {
          value:1,
          label:"微小"
        },
        {
          value:2,
          label:"一般"
        },
        {
          value:3,
          label:"严重"
        },
      ],


      taskData: [
        {
          taskid: '1',
          tasksize: '65',
          taskprocess: '45',
          taskstate: '进行中'
        },
        {
          taskid: '2',
          tasksize: '45',
          taskprocess: '45',
          taskstate: '进行中'
        },
        {
          taskid: '3',
          tasksize: '65',
          taskprocess: '0',
          taskstate: '进行中'
        }
      ],
      bugData: [
        {
          bugid: '3',
          bugstate: '未审核',
        },
        {
          bugid: '3',
          bugstate: '未审核',
        },
        {
          bugid: '3',
          bugstate: '未审核',
        },
        {
          bugid: '3',
          bugstate: '未审核',
        },
        {
          bugid: '3',
          bugstate: '未审核',
        },
        {
          bugid: '3',
          bugstate: '未审核',
        }
      ],
      bugdetail: {
        id: '3',
        level: '',
        type: '',
        comments: ''
      }
    }
  },
  created() {
    this.totalItems = this.bugData.length;
    if (this.totalItems > this.pageSize) {
      for (let index = 0; index < this.pageSize; index++) {
        this.tableDataEnd.push(this.bugData[index]);
      }
    } else {
      this.tableDataEnd = this.bugData;
    }
  },
  mounted(){
    //页面初始化方法
    var groupurl = '/getgroups';
    this.$ajax.get(groupurl).then((res) => {

      var grouplist = res.data;
      var items = [];
      for(var i=0; i < grouplist.length; i++){
        items[i] = {taskid: grouplist[i].taskid , tasksize: grouplist[i].tasksize, taskprocess: grouplist[i].taskprocess};
      }
      this.taskData = items;
    });
  },
  methods: {
    loadbugdata(){
      this.totalItems = this.bugData.length;
      if (this.totalItems > this.pageSize) {
        for (let index = 0; index < this.pageSize; index++) {
          this.tableDataEnd.push(this.bugData[index]);
        }
      } else {
        this.tableDataEnd = this.bugData;
      }
    },
    tagtask(index, row) {
      console.log("start tag task: "+ index);
      var bugsurl = '/getbugs';
      this.$ajax.get(bugsurl, {
        params: {
          id: index
        }
      }).then((res) => {
        var bugslist = res.data;
        var items = [];
        for(var i=0; i < bugslist.length; i++){
          var state = '待审核';
          if(bugslist[i].isReviewed){
            state = '已审核';
          }
          items[i] = {bugid:bugslist[i].id, bugstate:state}
        }
        this.bugData = items;
        this.tableDataEnd = [];
        this.loadbugdata();

        for (var i = 0; i < this.taskData.length; i++) {
          if (this.taskData[i].taskid === index) {
            var clustersize = this.taskData[i].tasksize;
            var clusterprocess = this.taskData[i].taskprocess;
            var clusterstate = this.taskData[i].taskstate;
            this.setClusterInfo(index, clustersize,clusterprocess,clusterstate)
          }
        }

        this.activeName = 'buglist';
      });
    },
    setClusterInfo(id, size, process, state){
      document.getElementById("clusterId").innerText = "聚类类簇ID：" + id;
      document.getElementById("clusterSize").innerText = "聚类采样数量：" + size;
      document.getElementById("clusterProcess").innerText = "采样标注进度：" + process;
      var tmpstate = "进行中";
      if(process === 0){
        tmpstate = "未进行";
      }
      else if(process === size){
        tmpstate = "已完成";
      }
      document.getElementById("clusterState").innerText = "类簇标注状态：" + tmpstate;



    },
    getdetails(index) {
      console.log("查看详情，id: "+ index);
      var bugdetailurl = '/getonebug';
      this.$ajax.get(bugdetailurl, {
        params: {
          id: index
        }
      }).then((res) => {
        this.activeName = 'bugdetail';
        // set id, type, level, location
        this.bugdetail.id = res.data.id;
        this.bugdetail.level = Number(res.data.threatlevel);
        this.bugdetail.type = res.data.type;
        this.bugdetail.comments = res.data.comment;

        // set code
        var path = res.data.path;
        var codeurl= "/getcode";
        this.$ajax.get(codeurl, {
          params: {
            id: index,
            path: path
          }
        }).then((coderes) => {
          var codetext = coderes.data;
          this.codedetail = codetext;
        });


        //后端添加接口
        var path = res.data.path;
        var sliceurl= "/getslice";
        this.$ajax.get(sliceurl, {
          params: {
            id: index,
            path: path
          }
        }).then((sliceres) => {
          var slicetext = sliceres.data;
          this.slicedetail = slicetext;
        });

      });
    },
    reviewpass(index, row) {
      console.log("审核通过，id: "+ index);
      var reviewpassurl = '/reviewpass';
      this.$ajax.get(reviewpassurl, {
        params: {
          id: index
        }
      }).then((res) => {
        row.bugstate = '已审核';
      });
    },
    reviewfalse(index, row) {
      console.log("审核误报，id: "+ index);
      // row.bugstate = '已审核';
      var reviewfalseurl = '/reviewfalse';
      this.$ajax.get(reviewfalseurl, {
        params: {
          id: index
        }
      }).then((res) => {
        row.bugstate = '已审核';
      });
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.pageSize = val;
      this.handleCurrentChange(1);
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.currentPage = val;
      //需要判断是否检索
      if (!this.flag) {
        this.currentChangePage(this.bugData);
        console.log(this.tableDataEnd);
      } else {
        this.currentChangePage(this.filterTableDataEnd);
        console.log(this.tableDataEnd);
      }
    },//组件自带监控当前页码
    currentChangePage(list) {
      let from = (this.currentPage - 1) * this.pageSize;
      let to = this.currentPage * this.pageSize;
      this.tableDataEnd = [];
      for (; from < to; from++) {
        if (list[from]) {
          this.tableDataEnd.push(list[from]);
        }
      }
    },
    confirmpass() {
      var bugid = this.bugdetail.id;
      console.log("审核通过，id: "+ bugid);
      var reviewpassurl = '/reviewpass';
      this.$ajax.get(reviewpassurl, {
        params: {
          id: bugid
        }
      }).then((res) => {

        //提交类型信息

        // let [llable,lvalue] = this.bugdetail.level;
        // var correctlevel = lvalue;
        var levellabel = this.$refs.itemSelect.selectedLabel;
        var correctlevel = 1;
        if(levellabel === '严重'){
          correctlevel = 3;
        }
        else if(levellabel === '一般'){
          correctlevel = 2;
        }

        var typeinfo = this.$refs['cascaderType'].getCheckedNodes()[0].label;
        var correcttype = typeinfo;
        var correctcomment = this.bugdetail.comments;
        var submiturl= "/submitbug";
        this.$ajax.get(submiturl, {
          params: {
            id: bugid,
            type: correcttype,
            level: correctlevel,
            comments: correctcomment
          }
        }).then((rst) => {
          console.log("success submit");
          console.log("comment: "+correctcomment);
        });


        this.reviewsuccess();
        const sleep = (milliseconds) => {
          return new Promise(resolve => setTimeout(resolve, milliseconds))
        };
        sleep(1000).then(() => {

          this.bugdetail.id = '';
          this.bugdetail.type = '';
          this.bugdetail.level = '';
          this.bugdetail.comments = '';
          this.codedetail = '';
          this.slicedetail = '';
          //do stuff
          this.activeName = 'buglist';

          for (var i = 0; i < this.bugData.length; i++) {
            if (this.bugData[i].bugid === bugid) {
              this.bugData[i].bugstate = '已审核';
            }
          }
        });
      });
    },
    confirmfalse() {
      var bugid = this.bugdetail.id;
      console.log("审核误报，id: "+ bugid);
      // row.bugstate = '已审核';
      var reviewfalseurl = '/reviewfalse';
      this.$ajax.get(reviewfalseurl, {
        params: {
          id: bugid
        }
      }).then((res) => {
        //提交类型信息
        // let [llable,lvalue] = this.bugdetail.level;
        // var correctlevel = lvalue;

        var levellabel = this.$refs.itemSelect.selectedLabel;
        var correctlevel = 1;
        if(levellabel === '严重'){
          correctlevel = 3;
        }
        else if(levellabel === '一般'){
          correctlevel = 2;
        }




        var typeinfo = this.$refs['cascaderType'].getCheckedNodes()[0].label;
        var correcttype = typeinfo;
        var correctcomment = this.bugdetail.comments;
        var submiturl= "/submitbug";
        this.$ajax.get(submiturl, {
          params: {
            id: bugid,
            type: correcttype,
            level: correctlevel,
            comments: correctcomment
          }
        }).then((rst) => {
          console.log("success submit");
          console.log("comment: "+correctcomment);
        });
        this.reviewsuccess();
        const sleep = (milliseconds) => {
          return new Promise(resolve => setTimeout(resolve, milliseconds))
        };
        sleep(1000).then(() => {

          this.bugdetail.id = '';
          this.bugdetail.type = '';
          this.bugdetail.level = '';
          this.bugdetail.comments = '';
          this.codedetail = '';
          this.slicedetail = '';




          this.activeName = 'buglist';
          for (var i = 0; i < this.bugData.length; i++) {
            if (this.bugData[i].bugid === bugid) {
              this.bugData[i].bugstate = '已审核';
            }
          }
        });
      });
    },
    reviewsuccess() {
      this.$message({
        message: '审核成功',
        type: 'success'
      });
    },


    starttag(index, row) {
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

};
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
.left-box{
  width: 50%;
  height: 100%;
  float: left;//设置两个盒子float:left
}
.right-box{
  width: 45%;
  height: 100%;
  float: right;//设置两个盒子float:left
}
.left-side{
  width: 65%;
  height: 100%;
  float: left;//设置两个盒子float:left
}
.right-side{
  width: 35%;
  height: 100%;
  float: right;//设置两个盒子float:left
}
</style>


<style>
  .text {
    font-size: 14px;
  }

  .item {
    margin-bottom: 18px;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  .clearfix:after {
    clear: both
  }

  .box-card {
    width: 480px;
  }
</style>
