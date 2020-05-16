<template>
  <el-tabs  v-model="activeName">
    <el-tab-pane label="众包任务列表" name="tasklist">
      <el-table
        :data="taskData"
        style="width: 100%"
        max-height="500">
        <el-table-column
          fixed
          prop="taskid"
          label="任务编号"
          width="200">
        </el-table-column>
        <el-table-column
          prop="tasksize"
          label="任务大小"
          width="200">
        </el-table-column>
        <el-table-column
          prop="taskprocess"
          label="工作进度"
          width="200">
        </el-table-column>
        <el-table-column prop="taskstate" label="任务状态" width="200">
          <template scope="scope">
            <span v-if="scope.row.taskprocess === 0 " style="color: #909399">未进行</span>
            <span v-else-if="scope.row.tasksize === scope.row.taskprocess" style="color: #67C23A">已完成</span>
            <span v-else style="color: #E6A23C">进行中</span>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="120">
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
      <el-table
        :data="tableDataEnd"
        style="width: 100%"
        max-height="500">
        <el-table-column
          fixed
          prop="bugid"
          label="漏洞编号"
          width="200">
        </el-table-column>
        <el-table-column prop="bugstate" label="审核状态" width="200">
          <template scope="scope">
            <span v-if="scope.row.bugstate==='已审核'" style="color: #4fde4f">已审核</span>
            <!--              <span v-else-if="scope.row.bugstate==='待审核'">待审核</span>-->
            <span v-else style="color: #ff0084">待审核</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="bugdetails"
          label="漏洞详情"
          width="200">
          <template slot-scope="scope">
            <el-button
              @click.native.prevent="getdetails(scope.row.bugid)"
              type="text"
              size="small">
              漏洞详情
            </el-button>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="400">
          <template slot-scope="scope">
            <el-button @click="reviewpass(scope.row.bugid, scope.row)" type="success" plain>审核通过</el-button>
            <el-button @click="reviewfalse(scope.row.bugid, scope.row)" type="warning" plain>标记误报</el-button>
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
              <el-input v-model="bugdetail.level" readonly="true" style="width:400px" align="left"></el-input>
            </el-form-item>
            <el-form-item label="漏洞类型">
              <el-input v-model="bugdetail.type" readonly="true" style="width:400px" align="left"></el-input>
            </el-form-item>
            <el-form-item label="相关备注">
              <el-input type="textarea" readonly="true" v-model="bugdetail.comments" style="width:400px" :rows="4"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="success" @click="confirmpass()">审核通过</el-button>
              <el-button type="warning" @click="confirmfalse()">标记误报</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="right-box">
          <el-divider style="font-size: 40px">相关源码</el-divider>
          <el-input
            type="textarea"
            :rows="13"
            readonly="true"
            placeholder=""
            v-model="codedetail">
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
    data() {
      return {
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
          }, {
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
          },{
            value: 'daohang',
            label: 'None',
            children: [{
              value: 'cexiangdaohang',
              label: 'None'
            }]
          }],


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
            taskid: '2016-05-03',
            tasksize: '65',
            taskprocess: '0',
            taskstate: '进行中'
          }
        ],
        bugData: [

        ],
        bugdetail: {
          id: '',
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
        var items = new Array();
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
          var items = new Array();
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
          this.activeName = 'buglist';
        });
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
          var level = "微小";
          if(res.data.threatlevel === 2){
            level = "一般";
          }
          else if(res.data.threatlevel === 3){
            level = "严重";
          }
          this.bugdetail.level = level;
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
