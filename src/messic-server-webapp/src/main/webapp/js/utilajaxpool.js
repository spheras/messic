/**
 * @source: https://github.com/spheras/messic
 *
 * @licstart  The following is the entire license notice for the
 *  JavaScript code in this page.
 *
 * The JavaScript code in this page is free software: you can
 * redistribute it and/or modify it under the terms of the GNU
 * General Public License (GNU GPL) as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option)
 * any later version.  The code is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU GPL for more details.
 *
 * As additional permission under GNU GPL version 3 section 7, you
 * may distribute non-source (e.g., minimized or compacted) forms of
 * that code without the copy of the GNU GPL normally required by
 * section 4, provided you include this license notice and a URL
 * through which recipients can access the Corresponding Source.
 *
 * @licend  The above is the entire license notice
 * for the JavaScript code in this page.
 *
 */
var UtilAjaxPool = function () {
    /** this indicates the maximum of elements that are running in parallel */
    this.maxElements = 5;
    this.started = false;
    this.pendingElements = new Array();
    this.runningElements = new Array();
    this.endFunction;
    this.executedElements = 0;
    this.totalElements = 0;

    /**
     * set the function to be execute when all the items has been processed
     * @param end: function to be executed
     */
    this.setEndFunction = function (endf) {
        this.endFunction = endf;
    }

    this.setMaxElements = function (max) {
        this.maxElements = max;
    }

    /** start running the ajax processes */
    this.start = function () {
        this.totalElements = this.pendingElements.length;
        this.executedElements = 0;
        this.started = true;
        this.refreshStatus();
    }


    /** cancel the pending elements */
    this.cancel = function () {
        this.pendingElements = new Array();
        this.started = false;
        this.refreshStatus();
        this.executedElements = 0;
    }

    /** Add an ajax process element */
    this.addProcess = function (method, datatype, url, successFunction, errorFunction, xhrFunction) {
        var element = {
            _method: method,
            _datatype: datatype,
            _url: url,
            _successFunction: successFunction,
            _errorFunction: errorFunction,
            _xhrFunction: xhrFunction,
        }
        this.pendingElements.push(element);
        this.refreshStatus();
        this.totalElements = this.totalElements + 1;
    }

    /** Check the pool, and see if it's necessary to execute a new one or not */
    this.refreshStatus = function () {
        if (this.started) {
            while (this.runningElements.length < this.maxElements && this.pendingElements.length > 0) {
                //we need to put another element
                var element = this.pendingElements.splice(0, 1);
                this.runElement(element[0]);
            }

            if (this.pendingElements.length == 0 && this.executedElements == this.totalElements) {
                //nothing more!
                if (this.endFunction) {
                    this.endFunction();
                }
            }
        }
    }

    /** run an element */
    this.runElement = function (element) {
        this.runningElements.push(element);
        var uploadPoolSelf = this;
        $.ajax({
            async: true,
            url: element._url,
            dataType: element._datatype,
            type: element._method,

            //Ajax events
            success: function (data) {
                uploadPoolSelf.executedElements = uploadPoolSelf.executedElements + 1;
                element._successFunction(data);
                uploadPoolSelf.runningElements.splice(0, 1);
                uploadPoolSelf.refreshStatus();
            },
            error: function (request, status, error) {
                uploadPoolSelf.executedElements = uploadPoolSelf.executedElements + 1;
                element._errorFunction(request, status, error);
                uploadPoolSelf.runningElements.splice(0, 1);
                uploadPoolSelf.refreshStatus();
            },
            xhr: element._xhrFunction,
            //data: bin
        });
    }
}