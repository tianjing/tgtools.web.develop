/**
 * 一个websocket消息处理套路
 * 不订阅消息，只是针对未知消息不处理，不会存储未处理的消息
 * @param url websocket url
 * @constructor
 */
function NotifyWebSocket(url, option) {
    this._option = {"url": url, "callback": {}};
    this._option["url"] = url;

    if(option) {
        if (option.onerror) {
            this._option.onerror = option.onerror;
        }
        if (option.onclose) {
            this._option.onclose = option.onclose;
        }
        if (option.onopen) {
            this._option.onopen = option.onopen;
        }
        if (option.onErrorMessage) {
            this._option.onErrorMessage = option.onErrorMessage;
        }
    }

    var _isOpen = false;
    var that = this;
    this.isOpen = function () {
        return _isOpen;
    }
    /**
     * 发送一个消息 给命令处理器处理
     * @param username 用户名
     * @param token 用户token
     * @param command 命令
     * @param data 数据
     */
    this.sendNotify = function (username, token, command, data) {
        var mydata = {"token": token, "user": username, "operation": command, "data": data};
        that._wstext.send(JSON.stringify(mydata))
    };
    /**
     * 订阅一个消息类型（message.type）
     * @param name 类型名称
     * @param func 处理方法（function）
     */
    this.subscribe = function (name, func) {
        this._option.callback[name] = func;
    };
    /**
     * 解除订阅
     * @param name
     * @param func
     */
    this.unSubscribe = function (name, func) {
        delete this._option.callback[name];
    };

    this._wstext = new WebSocket(this._option.url);
    this._wstext.onopen = function () {
        console.log('onopen');
        _isOpen = true;
        if (that._option.onopen) {
            that._option.onopen();
        }
    };
    this._wstext.onerror = function (e) {
        console.log('onerror');
        if (that._option.onerror) {
            that._option.onerror(e);
        }
    }
    this._wstext.onmessage = function (e) {
        console.log("onmessage");
        var data = eval("(" + e.data + ")");
        if (data.type && that._option.callback[data.type]) {
            that._option.callback[data.type](data.content);
        }
        else if (!data.type && data.error) {
            if (that._option.onErrorMessage) {
                that._option.onErrorMessage(data);
            }
            else {
                alert(data.error);
            }
        }else {
            console.log("no process:" +e.data);
        }
    };

    this._wstext.onclose = function (a, b) {
        _isOpen = false;
        console.log('onclose');
        if (that._option.onclose) {
            that._option.onclose(a, b);
        }
    };
};