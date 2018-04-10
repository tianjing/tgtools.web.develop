function NotifyWebSocket(url) {
    this._option = {"url": url, "callback": {}};
    this._option["url"] = url;
    var _isOpen = false;
    var that = this;
    this.isOpen = function () {
        return _isOpen;
    }

    this.sendNotify = function (username, token, command, data) {
        var mydata = {"token": token, "user": username, "operation": command, "data": data};
        that._wstext.send(JSON.stringify(mydata))
    };
    this.subscribe = function (name, func) {
        this._option.callback[name] = func;
    };
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
        if (that._option.onclose) {
            that._option.onclose(e);
        }
    }
    this._wstext.onmessage = function (e) {
        console.log("onmessage");
        var data = eval("(" + e.data + ")");
        if (that._option.callback[data.type]) {
            that._option.callback[data.type](data.content);
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