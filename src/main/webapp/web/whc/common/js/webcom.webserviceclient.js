
(function ($) {
    $.extend(true, window, {
        "webcom": {
            "DataMessageSender": DataMessageSender,
            "DataMessage": DataMessage,
            "DataSet": DataSet,
            "WebServiceClient": WebServiceClient,
            "json2str": json2str,
            "jsonText2DataMessage": jsonText2DataMessage
        }});
    /**
     * 生成web服务客户端
     */
    function WebServiceClient(_serviceName, _componentName) {
        this.request = new webcom.DataMessageSender();
        this.request.setWebServiceName(_serviceName);
        this.request.addParameter("componentName", _componentName);
        //事件
        WebServiceClient.prototype.onLoadComplete;
    }

    WebServiceClient.prototype.loadRemoteData = function (_queryName, _pageRowCount, _pageNum, _requestPropertyNames,_crossDomain) {
        this.request.stopAjaxRequest();
        if(_pageRowCount>0){
            this.request.addParameter("pageRowCount", _pageRowCount||99999999);
        }
        this.request.addParameter("queryName", _queryName);
        this.request.addParameter("pageIndex", _pageNum||'1');
        this.request.addParameter("requestPropertyNames", _requestPropertyNames);
        if(_crossDomain){
            this.request.setCrossDomain(_crossDomain);
        }
        var self = this;
        var _ajaxObj = this.request.sendJSON(function (_json) {
            var _response=null;
            try{
                _response = eval("(" + _json + ")");
            }catch(e){
                webcom.alert("错误","数据加载失败","error");
            }

            //{pageCount: "2", pageIndex: "1", pageRowCount: "10", totalRowCount: "20"}
            //self.pageCount= _response.parameterSet.pageCount;
            //self.totalRowCount=_response.parameterSet.totalRowCount;
            //self.data = _response.dataSet[0].entityList;

            if (_response && _response.dataSet[0]) {
                if (self.onLoadComplete) {
                    self.onLoadComplete(_response.parameterSet, _response.dataSet[0].entityList);
                }
            } else {
                if(self.onLoadError){
                    self.onLoadError(_response);
                }
            }
        });
    }
    WebServiceClient.prototype.getHttpRequest = function () {
        return this.request;
    }
    /**
     *  生成请求报文对象<br>
     *  @type function
     *  @namespace
     *  @example
     *  var dataset = new webcom.DataMessageSender();
     */
    function DataMessageSender() {
        var _self = this;
        var _scriptIndex = 1;
        var _type = "request";
        var _webServiceName = "";
        var _dataMessageObj = new webcom.DataMessage();
        var _xhr = {};
        var _strJsonDatamessageService = "/dataMessageService?_format=json";
        var _strJsonCrossDomainProxyService = webcom.APP_PATH + "/jsonCrossDomainProxyService";
        var _crossDomain = "";

        $.extend(this, {
            "setCrossDomain": setCrossDomain,												//设置跨域
            "setWebServiceName": setWebServiceName,								            //设置服务名
            "addParameter": addParameter,													//添加参数
            "addParameterList": addParameterList,											//添加参数
            "addDataSet": addDataSet,														// 添加dataset参数
            "addJsonDataSet": addJsonDataSet,												//添加json格式的dataset参数
            "setDataMessage": setDataMessage,												//设置datamessage参数
            "getDataMessage": getDataMessage,												//获取datamessage参数
            "toJson": toJson,																//把请求报文对象转化成字符串
            "sendJSON": sendJSON,															//发送异步json
            "sendSyncJSON": sendSyncJSON,													//发送同步json
            "sendServerCrossDomain": sendServerCrossDomain,						            //服务器跨域
            "sendSyncServerCrossDomain": sendSyncServerCrossDomain,	                        //服务器同步跨域
            "sendJsCrossDomain": sendJsCrossDomain,										    //js跨域，对于大数据请求不支持
            "encodeDataAccessUrl": encodeDataAccessUrl,
            "stopAjaxRequest": stopAjaxRequest,												//终止ajax请求
            "clearParameter": clearParameter
        });

        /**
         *  设置跨域<br>
         *  @type function
         *  @param {String} crossDomain
         */
        function setCrossDomain(crossDomain) {
            _crossDomain = crossDomain;
        }

        /**
         *  设置服务名<br>
         *  @type function
         *  @param {String} webServiceName
         */
        function setWebServiceName(_webServiceName) {
            _dataMessageObj.setWebServiceName(_webServiceName);
        }

        /**
         *  添加参数<br>
         *  @type function
         *  @param {String} name
         *  @param {String} value
         */
        function addParameter(name, value) {
            _dataMessageObj.addParameter(name, value);
        }

        function clearParameter() {
            for (var _key in _dataMessageObj.parameterSet) {
                delete _dataMessageObj.parameterSet[_key];
            }
            //_dataMessageObj.parameterSet={};
        }

        /**
         *  添加参数<br>
         *  @type function
         *  @param {Array} parameterSet
         */
        function addParameterList(_parameterSet) {
            _dataMessageObj.addParameterList(_parameterSet);
        }

        /**
         *  添加dataset参数<br>
         *  @type function
         *  @param {Object} dataset
         */
        function addDataSet(dataset) {
            _dataMessageObj.addDataSet(dataset);
        }

        /**
         *  添加json格式的dataset参数<br>
         *  @type function
         *  @param {Object} jsondataset
         */
        function addJsonDataSet(jsondataset) {
            _dataMessageObj.addJsonDataSet(jsondataset);
        }

        /**
         *  设置datamessage参数<br>
         *  @type function
         *  @param {Object} datamessage
         */
        function setDataMessage(datamessage) {
            _dataMessageObj.setDataMessage(datamessage);
        }

        function getDataMessage() {
            return _dataMessageObj.getData();
        }

        /**
         *  把请求报文对象转化成字符串<br>
         *  @type function
         *  @return 返回json字符串
         */
        function toJson() {
            //alert(__dataMessageObj.toJson());
            //window.prompt('',__dataMessageObj.toJson());
            var _jsonText = escape(_dataMessageObj.toJson());
            try {
                return _jsonText;
            } finally {
                _jsonText = null;
                delete _jsonText;
            }
        }

        /**
         * 发送异步json<br>
         *  @type function
         *  @param {function} cb
         */
        function sendJSON(cb) {

            if (typeof _crossDomain != "undefined" && _crossDomain != "") {
                _xhr = this.sendServerCrossDomain(cb);
                return _xhr;
            } else {
                _xhr = $.post(_self.encodeDataAccessUrl(_strJsonDatamessageService), this.toJson(), function (_jsonText) {
                    //alert(JSON);
                    cb(_jsonText);
                    _jsonText = null;
                    delete _jsonText;
                });
                return _xhr;
            }
        }

        /**
         * 发送异步json<br>
         *  @type function
         *  @param {function} _callBackFunc
         */
        function _sendJSON(_callBackFunc) {
            if (typeof _crossDomain != "undefined" && _crossDomain != "") {
                _xhr = this.sendServerCrossDomain(_callBackFunc);
                return _xhr;
            } else {
                _xhr = $.post(_self.encodeDataAccessUrl(_strJsonDatamessageService), this.toJson(), function (_jsonText) {
                    var _responseDataMessage = new webcom.DataMessage(_jsonText);
                    _callBackFunc(_responseDataMessage);
                    _jsonText = null;
                    delete _jsonText;
                });
                return _xhr;
            }
        }

        /**
         * 发送同步json<br>
         *  @type function
         *  @param {function} _callBackFunc
         */
        function sendSyncJSON(_callBackFunc) {

            if (typeof _crossDomain != "undefined" && _crossDomain != "") {
                return this.sendSyncServerCrossDomain();
            } else {
                var _responseText = $.ajax({
                    type: "POST",
                    url: _self.encodeDataAccessUrl(_strJsonDatamessageService),
                    data: this.toJson(),
                    async: false
                }).responseText;

                try {
                    if (!_callBackFunc)return _responseText;
                    _callBackFunc(_responseText);
                } finally {
                    _responseText = null;
                    delete _responseText;
                }
            }
        }

        /**
         * 服务器跨域<br>
         *  @type function
         *  @param {function} _callBackFunc
         */
        function sendServerCrossDomain(_callBackFunc) {
            //跨域URL
            //设置代理服务
            var _url = _strJsonCrossDomainProxyService;

            if (_url.indexOf('?') > 0)
                _url = _url + "&_proxy_domain=" + escape(_crossDomain);
            else
                _url = _url + "?_proxy_domain=" + _crossDomain;

            _xhr = $.post(_self.encodeDataAccessUrl(_url), this.toJson(), function (_jsonText) {
                _callBackFunc(_jsonText);
                JSON = null;
                delete JSON;
            });

            return _xhr;
        }

        /**
         * 服务器同步跨域<br>
         *  @type function
         */
        function sendSyncServerCrossDomain() {
            //跨域URL
            //this.addParameter("url",__crossDomain);
            //设置代理服务
            var _url = _strJsonCrossDomainProxyService;

            if (_url.indexOf('?') > 0)
                _url = _url + "&_proxy_domain=" + escape(_crossDomain);
            else
                _url = _url + "?_proxy_domain=" + _crossDomain;

            var _responseText = $.ajax({
                type: "POST",
                url: _self.encodeDataAccessUrl(_url),
                data: this.toJson(),
                async: false
            }).responseText;
            try {
                return _responseText;
            } finally {
                _responseText = null;
                delete _responseText;
            }
        }

        //js跨域，对于大数据请求不支持
        function sendJsCrossDomain(_callBackFunc) {
            var __scriptIndex = "script" + _scriptIndex++;
            _xhr = $.getScript(url + "/" + window.componentName + "/proxy/DataMessageProxy.jsp?json=" + this.toJson() + "&id=" + __scriptIndex, function () {
                if (typeof _callBackFunc == "string") {
                    //eval(eval(cb+"(scriptId)"));
                    eval(_callBackFunc + "(__scriptIndex);");
                } else {
                    _callBackFunc(eval(__scriptIndex));
                }
            });
            return _xhr;
        }

        function encodeDataAccessUrl(_url) {
            return _url;
        }

        //终止ajax请求
        function stopAjaxRequest() {
            if (_xhr != null && _xhr.abort) {
                _xhr.abort();
                _xhr = {};
            }
        }
    }

    /**
     *  生成DataSet数据结构<br>
     *    格式:{metadata:[{}],entity:[],name:''};
     *  @type function
     *  @namespace
     *  @example
     *  var dataset = new webcom.DataSet();
     */
    function DataSet() {
        var _dataSet = {};
        _dataSet.name = "";
        _dataSet.metadata = [
            {}
        ];
        _dataSet.entityList = [];
        $.extend(this, {
            "name": _dataSet.name,
            "metadata": _dataSet.metadata,
            "entityList": _dataSet.entityList,
            "setName": setName,							//设置dataSet _name
            "setMetaData": setMetaData,			//设置metadata参数
            "addMetaData": addMetaData,			// 添加metadata参数
            "addEntity": addEntity,						//添加entity参数
            "addEntityList": addEntityList,			// 添加entity参数
            "toJson": toJson,
            "getData": getData
        });

        /**
         *  设置dataSet _name<br>
         *  @type function
         *  @param {String} _name
         */
        function setName(_name) {
            _dataSet.name = _name;
        }

        /**
         *  设置metadata参数<br>
         *  @type function
         *  @param {Object} _metadata
         */
        function setMetaData(_metadata) {
            _dataSet.metadata[0] = _metadata;
        }

        /**
         *  添加metadata参数<br>
         *  @type function
         *  @param {String} _name
         *  @param {String} _value
         */
        function addMetaData(_name, _value) {
            _dataSet.metadata[0][_name] = "" + _value;
        }

        /**
         *  添加entity参数<br>
         *  @type function
         *  @param {Object} entity
         */
        function addEntity(_entity) {
            if (_entity instanceof Array) {
                _dataSet.entityList = _entity;
            } else {
                _dataSet.entityList[_dataSet.entityList.length] = _entity;
            }
        }

        /**
         *  添加entity参数<br>
         *  @type function
         *  @param {Object} entity
         */
        function addEntityList(_entityList) {
            if (_entityList instanceof Array) {
                _dataSet.entityList = _entityList;
            } else {
                _dataSet.entityList[_dataSet.entityList.length] = _entityList;
            }
        }

        function toJson() {
            var _jsonText = webcom.json2str(_dataSet);
            return _jsonText;
        }

        function getData() {
            return _dataSet;
        }
    }

    /**
     *  生成DataMessage数据结构<br>
     *    格式:{type:'request|response',parameterSet:[{}],dataSet:[]};
     *  @type function
     *  @namespace
     *  @example
     *  var datamessage = new webcom.DataMessage();
     */
    function DataMessage(_jsonText) {

        var _dataMessage = {};
        _dataMessage.type = "request";
        _dataMessage.messageType = "success";
        _dataMessage.message = "操作成功";
        _dataMessage.parameterSet = {};
        _dataMessage.dataSet = [];

        if (_jsonText && _jsonText != "") {
            var _dataMessageJson = eval("(" + _jsonText + ")");
            _dataMessage.messageType = _dataMessageJson.messageType;
            _dataMessage.message = _dataMessageJson.message;
            _dataMessage.parameterSet = _dataMessageJson.parameterSet;
            _dataMessage.dataSet = _dataMessageJson.dataSet;
        }
        $.extend(this, {
            "type": _dataMessage.request,
            "messageType": _dataMessage.messageType,
            "message": _dataMessage.message,
            "parameterSet": _dataMessage.parameterSet,
            "dataSet": _dataMessage.dataSet,

            "getMessageType": getMessageType,
            "getMessage": getMessage,
            "getParameter": getParameter,
            "addParameter": addParameter,
            "addParameterList": addParameterList,
            "getWebServiceName": getWebServiceName,
            "setWebServiceName": setWebServiceName,
            "setDataMessage": setDataMessage,
            "getDataSet": getDataSet,
            "addDataSet": addDataSet,
            "addJsonDataSet": addJsonDataSet,
            "toJson": toJson,
            "getData": getData
        });

        function getMessageType() {
            return _dataMessage.messageType;
        }

        function getMessage() {
            return _dataMessage.message;
        }

        function getParameter(_paraName) {
            return _dataMessage.parameterSet[_paraName];
        }

        /**
         *  添加参数<br>
         *  @type function
         *  @param {String} name
         *  @param {String} value
         *  @example
         *    var datamessage = new webcom.DataMessage();
         *    datamessage.addParameter("name","张三");
         */
        function addParameter(_paraName, _paraValue) {
            if (_paraName == null || _paraName == "")return;
            _paraName = _paraName.replace(/\./g, "__");
            try {
                if (typeof _paraValue == "undefined" || _paraValue == null || _paraValue == "undefined" || _paraValue == "null") _paraValue = "";
                _dataMessage.parameterSet[_paraName] = "" + _paraValue;
            } catch (e) {
                alert("json参数错误");
            }
        }

        /**
         *  添加参数<br>
         *  @type function
         *  @param {String} parameterSet
         *  @example
         *    var para = [{key:'name',value:'张三'},{key:'age',value:'20'}]
         *    var datamessage = new webcom.DataMessage();
         *    datamessage.addParameterList(para);
         */
        function addParameterList(_parameterList) {

            var _length = _parameterList.length;
            var i = 0;
            while (i < _length) {
                var _para = _parameterList[i];
                this.addParameter(_para.key, _para.value);
                i++;
            }
        }

        function getWebServiceName() {
            return _dataMessage.webServiceName;
        }


        /**
         *  添加参数<br>
         *  @type function
         *  @param {String} webServiceName
         */
        function setWebServiceName(_webServiceName) {
            _dataMessage.webServiceName = _webServiceName;
        }

        /**
         *  设置datamessage参数<br>
         *  @type function
         *  @param {Object} datamessage
         */
        function setDataMessage(dataMessage) {
            _dataMessage = dataMessage;
        }

        /**
         *  添加dataSet参数<br>
         *  @type function
         *  @param {Object} dataSet
         */
        function getDataSet(_name) {

            var _length = _dataMessage.dataSet.length;
            for (var i = 0; i < _length; i++) {
                var _dataSetName = _dataMessage.dataSet[i].name;
                if (_name == _dataSetName)
                    return _dataMessage.dataSet[i];
            }
            return null;
        }

        /**
         *  添加dataSet参数<br>
         *  @type function
         *  @param {Object} dataSet
         */
        function addDataSet(_dataSet) {
            _dataMessage.dataSet[_dataMessage.dataSet.length] = _dataSet.getData();
        }

        /**
         *  添加json格式的dataSet参数<br>
         *  @type function
         *  @param {Object} _dataSet
         */
        function addJsonDataSet(_dataSet) {
            _dataMessage.dataSet[_dataMessage.dataSet.length] = _dataSet;
        }

        function toJson() {
            var _jsonText = webcom.json2str(_dataMessage);
            return _jsonText;
        }

        function getData() {
            return _dataMessage;
        }
    }

    /**
     *  json对象转化成字符串
     *  @type function
     *    @param {String} o
     */
    function json2str(o) {
        if (o == null || typeof(o) == "undefined") {
            return "null";
        }
        var r = [];
        if (typeof o == "string") return "\"" + o.replace(/([\'\"\\])/g, "\\$1").replace(/(\n)/g, "\\n").replace(/(\r)/g, "\\r").replace(/(\t)/g, "\\t") + "\"";
        if (typeof o == "object") {
            if (!o.sort) {
                for (var i in o) {
                    r.push(i + ":" + webcom.json2str(o[i]));
                }
                var oString = o.toString;
                if (!!document.all && !/^\n?function\s*toString\(\)\s*\{\n?\s*\[native code\]\n?\s*\}\n?\s*$/.test(oString)) {
                    r.push("toString:" + oString.toString());
                }
                r = "{" + r.join() + "}"
            } else {
                var len = o.length;
                var i = 0;
                while (i < len) {
                    r.push(webcom.json2str(o[i]));
                    i++;
                }
                r = "[" + r.join() + "]"
            }
            try {
                return r;
            } finally {
                r = null;
                delete r;
            }
        }
        try {
            return o.toString();
        } finally {
            o = null;
            delete o;
        }
    }


    /**
     *  jsonText2DataMessage 解析DataMessage数据结构<br>
     *    格式:{type:'request|response',parameterSet:[{}],dataSet:[]};
     *  @type function
     *  @namespace
     *  @example
     *  var dataMessage = webcom.parseDataMessage();
     */
    function jsonText2DataMessage(_jsonText) {

        var _dataMessageJson = eval("(" + _jsonText + ")");
        var _dataMessage = {};
        _dataMessage.type = _dataMessageJson.type;
        _dataMessage.parameterSet = _dataMessageJson.parameterSet;
        _dataMessage.dataSet = _dataMessageJson.dataSet;
        return {
            "toJson": function () {
                var _jsonText = webcom.json2str(_dataMessage);
                return _jsonText;
            }
        };
    }


})($);