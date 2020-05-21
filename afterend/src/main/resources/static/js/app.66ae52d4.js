(function(t){function e(e){for(var r,s,l=e[0],c=e[1],i=e[2],d=0,v=[];d<l.length;d++)s=l[d],Object.prototype.hasOwnProperty.call(o,s)&&o[s]&&v.push(o[s][0]),o[s]=0;for(r in c)Object.prototype.hasOwnProperty.call(c,r)&&(t[r]=c[r]);u&&u(e);while(v.length)v.shift()();return n.push.apply(n,i||[]),a()}function a(){for(var t,e=0;e<n.length;e++){for(var a=n[e],r=!0,l=1;l<a.length;l++){var c=a[l];0!==o[c]&&(r=!1)}r&&(n.splice(e--,1),t=s(s.s=a[0]))}return t}var r={},o={app:0},n=[];function s(e){if(r[e])return r[e].exports;var a=r[e]={i:e,l:!1,exports:{}};return t[e].call(a.exports,a,a.exports,s),a.l=!0,a.exports}s.m=t,s.c=r,s.d=function(t,e,a){s.o(t,e)||Object.defineProperty(t,e,{enumerable:!0,get:a})},s.r=function(t){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(t,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(t,"__esModule",{value:!0})},s.t=function(t,e){if(1&e&&(t=s(t)),8&e)return t;if(4&e&&"object"===typeof t&&t&&t.__esModule)return t;var a=Object.create(null);if(s.r(a),Object.defineProperty(a,"default",{enumerable:!0,value:t}),2&e&&"string"!=typeof t)for(var r in t)s.d(a,r,function(e){return t[e]}.bind(null,r));return a},s.n=function(t){var e=t&&t.__esModule?function(){return t["default"]}:function(){return t};return s.d(e,"a",e),e},s.o=function(t,e){return Object.prototype.hasOwnProperty.call(t,e)},s.p="/";var l=window["webpackJsonp"]=window["webpackJsonp"]||[],c=l.push.bind(l);l.push=e,l=l.slice();for(var i=0;i<l.length;i++)e(l[i]);var u=c;n.push([0,"chunk-vendors"]),a()})({0:function(t,e,a){t.exports=a("56d7")},"56d7":function(t,e,a){"use strict";a.r(e);a("e260"),a("e6cf"),a("cca6"),a("a79d");var r=a("2b0e"),o=a("ce5b"),n=a.n(o),s=(a("bf40"),a("bc3a")),l=a.n(s),c=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("v-app",[a("v-app-bar",{attrs:{app:""},scopedSlots:t._u([{key:"extension",fn:function(){return[a("v-tabs",{attrs:{"align-with-title":""}},[a("v-tab",{attrs:{to:"/"}},[t._v("模型配置")]),a("v-tab",{attrs:{to:"/train"}},[t._v("模型训练")]),a("v-tab",{attrs:{to:"/predict"}},[t._v("模型预测")])],1)]},proxy:!0}])},[a("span",{staticClass:"headline"},[t._v("代码漏洞误报检测系统")])]),a("v-content",{attrs:{app:""}},[a("router-view")],1)],1)},i=[],u={name:"App"},d=u,v=a("2877"),p=Object(v["a"])(d,c,i,!1,null,null,null),h=p.exports,m=a("8c4f"),f=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("v-container",[a("v-radio-group",{model:{value:t.extractMethod,callback:function(e){t.extractMethod=e},expression:"extractMethod"}},[a("v-row",[a("v-col",{attrs:{cols:"12",md:"1"}},[a("span",{staticClass:"title"},[t._v("特征提取")])]),a("v-col",{attrs:{cols:"12",md:"2"}},[a("v-radio",{attrs:{label:"Word2Vector方法",value:"Word2Vec"}})],1),a("v-col",{attrs:{cols:"12",md:"2"}},[a("v-radio",{attrs:{label:"Paragraph2Vector方法",value:"Para2Vec"}})],1),a("v-col",{attrs:{cols:"12",md:"3"}},[a("v-radio",{attrs:{label:"DeepWalk算法",value:"DeepWalk"}})],1)],1)],1),a("v-row",[a("v-col",{attrs:{cols:"12",md:"1"}}),a("v-col",{attrs:{cols:"12",md:"8"}},[a("v-text-field",{attrs:{label:"特征提取方法说明",value:t.extractText,outlined:"",readonly:""}})],1)],1),a("v-radio-group",{model:{value:t.modelName,callback:function(e){t.modelName=e},expression:"modelName"}},[a("v-row",[a("v-col",{attrs:{cols:"12",md:"1"}},[a("span",{staticClass:"title"},[t._v("模型选择")])]),a("v-col",{attrs:{cols:"12",md:"2"}},[a("v-radio",{attrs:{disabled:"Word2Vec"!==t.extractMethod,label:"卷积神经网络(CNN)",value:"cnn"}})],1),a("v-col",{attrs:{cols:"12",md:"2"}},[a("v-radio",{attrs:{disabled:"Word2Vec"!==t.extractMethod,label:"长短期记忆模型(LSTM)",value:"lstm"}})],1),a("v-col",{attrs:{cols:"12",md:"3"}},[a("v-radio",{attrs:{disabled:"Word2Vec"===t.extractMethod,label:"图卷积神经网络(GCN)",value:"gcn"}})],1)],1)],1),a("v-row",[a("v-col",{attrs:{cols:"12",md:"1"}}),a("v-col",{attrs:{cols:"12",md:"8"}},[a("v-text-field",{attrs:{label:"机器学习模型说明",value:t.modelText,outlined:"",readonly:""}})],1)],1),a("v-row",[a("v-col",{attrs:{cols:"12",md:"1"}},[a("span",{staticClass:"title"},[t._v("特征向量大小")])]),a("v-col",{attrs:{cols:"12",md:"2"}},[a("v-select",{attrs:{items:t.featureItems,dense:"",outlined:""},model:{value:t.featureSize,callback:function(e){t.featureSize=e},expression:"featureSize"}})],1)],1),a("v-row",[a("v-col",{attrs:{cols:"12",md:"1"}},[a("span",{staticClass:"title"},[t._v("训练次数")])]),a("v-col",{attrs:{cols:"12",md:"2"}},[a("v-text-field",{attrs:{rules:t.numberRules,dense:"",outlined:""},model:{value:t.epochNum,callback:function(e){t.epochNum=e},expression:"epochNum"}})],1)],1),a("v-row",[a("v-col",{attrs:{cols:"12",md:"2"}},[a("v-btn",{attrs:{color:"primary",dense:""},on:{click:t.finish}},[t._v("配置完成")])],1)],1)],1)},b=[],g={name:"ConfigPage",data:function(){return{extractMethod:"Word2Vec",modelName:"cnn",cnn:!1,lstm:!1,deepwalk:!1,para2vec:!1,featureItems:[8,16,32],featureSize:16,epochNum:10,numberRules:[function(t){return!!t||"请输入数字"},function(t){return isFinite(t)||"必须输入数字"}]}},computed:{extractText:function(){switch(this.extractMethod){case"Word2Vec":return"Word2Vector方法可以将一段文本中的每个词映射为词向量";case"Para2Vec":return"Paragraph2Vector方法将每个基本块看做段落并转化为段落向量";case"DeepWalk":return"DeepWalk算法在向量转换时考虑了节点在图中的位置";default:return""}},modelText:function(){switch(this.modelName){case"cnn":return"卷积神经网络的基本组成是M-P神经元，由卷积层、池化层和全连接层组成，可以降低参数规模，提升运算效率，避免过度拟合";case"lstm":return"长短期记忆模型是一类循环神经网络模型，用于处理例如文本和语音这类序列数据";case"gcn":return"图卷积神经网络则是将卷积操作应用在图结构上，优化卷积核的参数";default:return""}}},methods:{finish:function(){var t=this,e="";"cnn"===this.modelName?e="cnn":"lstm"===this.modelName?e="lstm":"DeepWalk"===this.extractMethod?e="deepwalk":"Para2Vec"===this.extractMethod&&(e="para2vec"),this.$http.post("/addModel",{name:e,featureSize:this.featureSize,epochNum:this.epochNum,state:"未训练"}).then((function(){t.$router.push({path:"/train"})}))}}},x=g,y=Object(v["a"])(x,f,b,!1,null,"3d26a512",null),_=y.exports,w=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("v-container",[a("v-row",[a("v-col",{attrs:{cols:"12",md:"12"}},[a("v-data-table",{attrs:{headers:t.headers,items:t.desserts,"items-per-page":10},scopedSlots:t._u([{key:"item.state",fn:function(e){var r=e.item;return[a("v-chip",{attrs:{color:t.getColor(r.state),outlined:""}},[t._v(t._s(r.state))])]}},{key:"item.actions",fn:function(e){var r=e.item;return["未训练"===r.state?a("v-btn",{attrs:{color:"primary",dense:"",outlined:""},on:{click:function(e){return t.train(r)}}},[t._v("开始训练")]):t._e(),"已训练"===r.state?a("v-btn",{attrs:{color:"primary",dense:"",outlined:""},on:{click:function(e){return t.showDetail(r)}}},[t._v("查看结果")]):t._e()]}}])})],1)],1),a("v-bottom-sheet",{model:{value:t.dialog,callback:function(e){t.dialog=e},expression:"dialog"}},[a("v-card",{attrs:{color:"grey lighten-4"}},[a("v-container",[a("v-row",[a("v-col",{attrs:{cols:"12",md:"6"}},[a("div",{staticStyle:{width:"600px",height:"400px"},attrs:{id:"chart"}})]),a("v-col",{attrs:{cols:"12",md:"6"}},[a("div",{staticStyle:{width:"600px",height:"400px"},attrs:{id:"val_chart"}})])],1),a("v-row",[a("v-col",{attrs:{cols:"12",md:"2"}},[a("span",{staticClass:"title"},[t._v("验证集精确度："+t._s(t.val_value1))])])],1),a("v-row",[a("v-col",{attrs:{cols:"12",md:"2"}},[a("span",{staticClass:"title"},[t._v("验证集F1值："+t._s(t.val_value2))])])],1)],1),a("v-card-actions",[a("v-btn",{attrs:{color:"primary",text:""},on:{click:function(e){t.dialog=!1}}},[t._v(" 关闭 ")])],1)],1)],1)],1)},k=[],S=(a("b0c0"),{name:"TrainPage",data:function(){return{headers:[{text:"模型名称",value:"name",sortable:!0},{text:"向量大小",value:"featureSize",sortable:!1},{text:"训练轮次",value:"epochNum",sortable:!1},{text:"状态",value:"state",sortable:!1},{text:"操作",value:"actions",sortable:!1}],desserts:[],dialog:!1,val_value1:0,val_value2:0}},methods:{loadTable:function(){var t=this;this.$http.get("/trainList").then((function(e){t.desserts=e.data}))},getColor:function(t){switch(t){case"未训练":return"red";case"进行中":return"orange";case"已训练":return"green"}},train:function(t){var e=this;t.state="进行中",this.$http.post("/train",t).then((function(){e.loadTable()}))},showDetail:function(t){var e=this;this.dialog=!0;var r=a("313e");this.$http.post("/detail",t).then((function(a){var o=a.data,n={title:{text:t["name"]+"在训练集上的表现"},xAxis:{type:"value"},yAxis:{type:"value",min:0,max:1},series:[],legend:{data:["loss","accuracy"],align:"left",bottom:10,right:60}};n.series.push(e.toSeries(o["loss"],"loss")),n.series.push(e.toSeries(o["accuracy"],"accuracy")),r.init(document.getElementById("chart")).setOption(n),n.series=[],n.title.text=t["name"]+"在验证集上的表现",n.series.push(e.toSeries(o["val_loss"],"loss")),n.series.push(e.toSeries(o["val_accuracy"],"accuracy")),r.init(document.getElementById("val_chart")).setOption(n),e.val_value1=o["value1"],e.val_value2=o["value2"]}))},toSeries:function(t,e){for(var a=[],r=0;r<t.length;r++)a.push([r+1,t[r]]);return{name:e,data:a,type:"line",smooth:!0}}},created:function(){this.loadTable()}}),C=S,N=Object(v["a"])(C,w,k,!1,null,"129efa46",null),P=N.exports,M=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("v-container",[a("v-row",[a("v-col",{attrs:{cols:"12",md:"12"}},[a("v-data-table",{attrs:{headers:t.headers,items:t.desserts,"items-per-page":10,loading:t.loadFlag,"loading-text":"正在处理中……"},scopedSlots:t._u([{key:"no-data",fn:function(){return[t._l(t.modelName,(function(e){return a("v-checkbox",{key:e,attrs:{label:e,value:e},model:{value:t.modelChoose,callback:function(e){t.modelChoose=e},expression:"modelChoose"}})})),a("v-btn",{attrs:{color:"primary",disabled:t.predictButton,outlined:""},on:{click:t.predict}},[t._v("开始预测")])]},proxy:!0}])})],1)],1)],1)},O=[],j=(a("d3b7"),{name:"PredictPage",data:function(){return{headers:[{text:"文件名",value:"name",sortable:!0}],desserts:[],loadFlag:!1,predictButton:!1,modelName:[],modelChoose:[]}},methods:{predict:function(){var t=this;this.loadFlag=!0,this.predictButton=!0;for(var e=0;e<this.modelChoose.length;e++)this.headers.push({text:this.modelChoose[e],value:this.modelChoose[e],sortable:!1});this.$http.post("/predict",{models:this.modelChoose}).then((function(e){t.desserts=e.data})).finally((function(){t.loadFlag=!1}))}},created:function(){var t=this;this.$http.get("/modelName").then((function(e){t.modelName=e.data}))}}),T=j,V=Object(v["a"])(T,M,O,!1,null,"09edecf6",null),W=V.exports;r["default"].use(m["a"]);var $=[{path:"/",name:"ConfigPage",component:_},{path:"/train",name:"TrainPage",component:P},{path:"/predict",name:"PredictPage",component:W}],z=new m["a"]({routes:$}),D=z;r["default"].config.productionTip=!1,r["default"].use(n.a),r["default"].prototype.$http=l.a,l.a.defaults.baseURL="http:///47.97.106.46:8080/api",l.a.defaults.headers.post["Content-Type"]="application/json",new r["default"]({router:D,vuetify:new n.a({}),render:function(t){return t(h)}}).$mount("#app")}});
//# sourceMappingURL=app.66ae52d4.js.map