
jQuery.fn.lvjPageGrid = function(_configJson) {
	var myJqObj = this;
	//设置默认参数
	if(_configJson.striped==undefined){
		_configJson.striped=true;
	}
	if(_configJson.width==undefined){
		_configJson.width='auto';
	}
	if(_configJson.pagination==undefined){// 是否显示分页
		_configJson.pagination=true;
	}
	if(_configJson.pageType==undefined){
		_configJson.pageType="remote";//pageType:{remote:远程，即后台分页，物理分页；local：本地，即前台分页，逻辑分页}
	}
	myJqObj.attr("lvjSearchFormId",_configJson.searchFormId);
	myJqObj.attr("lvjUrl",_configJson.url);
	myJqObj.attr("lvjPagination",_configJson.pagination);//是否显示分页
	myJqObj.attr("lvjPageType",_configJson.pageType);//分页类型
	myJqObj.attr("lvjPageSqlId",_configJson.pageSqlId);//分页查询sqlId
	_configJson.url=null;
	
	myJqObj.datagrid(_configJson);
	// 设置分页控件
	if (_configJson.pagination && ("remote"==_configJson.pageType)) {// 显示分页
		var _pager = myJqObj.datagrid('getPager');
		_pager.pagination({
			from:0,
			to:0,
			total:0,
			pageSize : 15,// 每页显示的记录条数，默认为10
			pageList : [ 15, 30, 50, 100 ],// 可以设置每页记录条数的列表
			beforePageText : '第',// 页数文本框前显示的汉字
			afterPageText : '页 共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录 共 {total} 条记录',
			onBeforeRefresh : function() {
			},
			onSelectPage : function() {// 分页触发
				myJqObj.lvjGetPage();
			}
		});
	}else{// 不显示分页
		// 默认不查询
//		myJqObj.lvjGetPage();
	}
	return myJqObj;
};

jQuery.fn.lvjGetPage = function() {
	var myJqObj = this;
	var params = jQuery("#" + myJqObj.attr("lvjSearchFormId")).serialize();
	if (("true" == myJqObj.attr("lvjPagination")) && ("remote" == myJqObj.attr("lvjPageType")) 
			&& !jQuery.lvjIsNull(myJqObj.attr("lvjPageSqlId"))) { // 物理分页
		var pageOptions = myJqObj.datagrid('getPager').data("pagination").options;
		if (null != params) {
			params += "&pageInfo.pageSqlId=" + myJqObj.attr("lvjPageSqlId");
			params += "&pageInfo.pageIndex=" + pageOptions.pageNumber;
			params += "&pageInfo.pageSize=" + pageOptions.pageSize;
		} else {
			params += "pageInfo.pageSqlId=" + myJqObj.attr("lvjPageSqlId");
			params += "&pageInfo.pageIndex=" + pageOptions.pageNumber;
			params += "&pageInfo.pageSize=" + pageOptions.pageSize;
		}
	}
	myJqObj.datagrid("loading"); // 加屏蔽
	jQuery.ajax({
		type : "POST",
		dataType : "json",
		url : myJqObj.attr("lvjUrl"),
		data : params,
		success : function(result) {
			if(jQuery.lvjIsNull(result)||jQuery.lvjIsNull(result.pageData)){
//				$.messager.alert('操作提示', '没有查询到数据!', '');
				myJqObj.datagrid('loadData', {
					"total" : 0,
					"rows" : []
				});
				myJqObj.datagrid("loaded"); // 移除屏蔽
			}else{
				if("true" == myJqObj.attr("lvjPagination")){// 分页
					// 加入处理特殊列格式化逻辑。例如：model.id
					var dataArray = [];
					var opts = myJqObj.datagrid('getColumnFields');//展示列
					if(opts[0].indexOf(".")>0){
						$.each(result.pageData, function(key, value) {
							var dataJsonObj = {};
							$.each(opts, function(index,opt){
								dataJsonObj[opt]=eval("value."+opt);
							});
							dataArray.push(dataJsonObj);
						});
					}else{
						dataArray = result.pageData;
					}
					
					if ("local"==myJqObj.attr("lvjPageType")) { // 逻辑分页
						myJqObj.datagrid({loadFilter:myJqObj.lvjPagerFilter}).datagrid('loadData', {
							"total" : result.pageData.length,
							"rows" : dataArray
						});
					} else {
						myJqObj.datagrid('loadData', {// 物理分页
							"total" : result.pageInfoModel.totalDataSize,
							"rows" :  dataArray
						});
					}
				} else {// 不分页
					myJqObj.datagrid('loadData', {
						"rows" :  result.pageData
					});
				}
				myJqObj.datagrid("loaded"); // 移除屏蔽
			}
		},
		error : function(err) {
			$.messager.alert('操作提示', '获取信息失败...请联系管理员!', 'error');
			myJqObj.datagrid("loaded"); // 移除屏蔽
		}
	});
};


/**
 * 逻辑分页
 */
jQuery.fn.lvjLocalPage = function(data) {
	var myJqObj = this;
	myJqObj.datagrid({loadFilter:myJqObj.lvjPagerFilter}).datagrid('loadData', data);
};

jQuery.fn.lvjPagerFilter = function(data){
	if (typeof data.length == 'number' && typeof data.splice == 'function'){    // 判断数据是否是数组
        data = {
            total: data.length,
            rows: data
        }
    }
    var dg = $(this);
    var opts = dg.datagrid('options');
    var pager = dg.datagrid('getPager');
    pager.pagination({
        onSelectPage:function(pageNum, pageSize){
            opts.pageNumber = pageNum;
            opts.pageSize = pageSize;
            pager.pagination('refresh',{
                pageNumber:pageNum,
                pageSize:pageSize
            });
            dg.datagrid('loadData',data);
        }
    });
    if (!data.originalRows){
        data.originalRows = (data.rows);
    }
    var start = (opts.pageNumber-1)*parseInt(opts.pageSize);
    var end = start + parseInt(opts.pageSize);
    data.rows = (data.originalRows.slice(start, end));
    return data;
}

/**
 * 插入一行
 * _editDataGrid:需要编辑的表格
 * _editRowIndexs:正在编辑的行
 */
jQuery.fn.lvjInsertRow = function(_editRowIndexs){
	var _editDataGrid = this;
	// 行首添加一行
	if (_editDataGrid.lvjEndEditing(_editRowIndexs)){
		_editDataGrid.datagrid('insertRow', {
            index: 0,
            row: {}
        });
		_editDataGrid.datagrid('beginEdit', 0);
		_editRowIndexs = new Array();
		_editRowIndexs.push(0);// _editRowIndexs=0
	}
	return _editRowIndexs;
};
/**
 * 编辑行
 */
jQuery.fn.lvjEditRow = function(_editRowIndexs){
	var _editDataGrid = this;
	var rows = _editDataGrid.datagrid("getSelections");
	if(rows == null || rows.length < 1){
		jQuery.lvjMsg.selectNoData();
		return ;
	}
	var rowIndex = undefined;
	if (_editRowIndexs == undefined) _editRowIndexs = new Array();
	for(var i=0;i<rows.length;++i){
		rowIndex = _editDataGrid.datagrid('getRowIndex',rows[i]);
		_editDataGrid.datagrid("beginEdit", rowIndex);
		_editRowIndexs.push(rowIndex);
	}
	return _editRowIndexs;
};
/**
 * 取消编辑行
 */
jQuery.fn.lvjCancelEditRow = function(_editRowIndexs){
	var _editDataGrid = this;
	if (_editRowIndexs == undefined){return}
	_editDataGrid.datagrid('rejectChanges')
			.datagrid('unselectAll');
	_editRowIndexs = undefined;
	return _editRowIndexs;
};

/**
 * 校验编辑行是否通过验证
 */
jQuery.fn.lvjEndEditing = function(_editRowIndexs){
	var _editDataGrid = this;
	if (_editRowIndexs == undefined){return true}
	var isEndEdit = true;
	// 验证必填项
	for(var i=0;i<_editRowIndexs.length;i++){
		if (_editDataGrid.datagrid('validateRow', _editRowIndexs[i])){
			_editDataGrid.datagrid('endEdit', _editRowIndexs[i]);
		} else {
			isEndEdit = false;
			break;
		}
	}
	return isEndEdit;
};

/**
 * 删除操作
 */
jQuery.fn.lvjDeleteConfirm = function(_selectRows,_doDeleteFn){
	var _editDataGrid = this;
	if(_selectRows == null || _selectRows.length < 1){
		jQuery.lvjMsg.selectNoData();
		return ;
	}
	var _param = {};
	var _ids = new Array();
	for(var i=0;i<_selectRows.length;++i){
		if(_selectRows[i].id){// 新增的未进行保存的记录直接刷新页面删除
			_ids.push(_selectRows[i].id);
		}
	}
	// 删除刚新增的未提交到数据库的行记录
	if(_ids.length < 1){
		for(var i=0;i<_selectRows.length;++i){
			var _rowIndex = _editDataGrid.datagrid('getRowIndex',_selectRows[i]);
			_editDataGrid.datagrid("deleteRow", _rowIndex);
		}
		_editDataGrid.datagrid('acceptChanges');// 提交所有修改的数据，提交后的数据将不能再修改或者回滚。
		jQuery.lvjMsg.success();
		return ;
	}
	_param["ids"]=_ids;
	$.messager.confirm('删除数据','确定删除选中的记录？',function(r){
		if(r){
			_doDeleteFn(_selectRows,_param);
		}
	});
};
/**
 * 保存编辑的行
 */
jQuery.fn.lvjSaveEditRow = function(_editRowIndexs,_doSaveFn){
	var _editDataGrid = this;
	if (_editDataGrid.lvjEndEditing(_editRowIndexs)) {
		var _selectRows = _editDataGrid.datagrid('getChanges');
		if(_selectRows.length < 1){
			jQuery.lvjMsg.saveNoData();
			return;
		}
		_doSaveFn(_selectRows);
	}
};

/**
 * 设置列只读，不可以修改
 */
jQuery.fn.lvjEditRowReadOnly = function(_editRowIndexs, _columns){
	var _editDataGrid = this;
	// 设置列不可以修改
	for(var i=0;i<_editRowIndexs.length;i++){
		for (var j = 0; j < _columns.length; j++) {
			var _cellEdit = _editDataGrid.datagrid('getEditor', { index: _editRowIndexs[i], field: _columns[j] });
			var $input = _cellEdit.target; // 得到文本框对象
			$input.prop('readonly',true); // 设值只读
		}
	}
}

/**
 * 扩展easyui的验证
 */
jQuery.lvjVerification = function(){
	$.extend($.fn.validatebox.defaults.rules, {
		//验证汉子字 
	    CHS: {  
	        validator: function (value) {  
	            return /^[\u0391-\uFFE5]+$/.test(value);  
	        },  
	        message: '只能输入汉字'  
	    },
	    //全部手机验证(4为数据卡)
	    mobile: {//value值为文本框中的值  
	        validator: function (value) {  
	            var reg = /^1[3458][0-9]{9}$/;  
	            return reg.test(value);  
	        },  
	        message: '输入手机号码格式不准确.'  
	    },
	    //邮政编码的验证
	    zipcode: {  
	        validator: function (value) {  
	            var reg = /^[1-9]\d{5}$/;  
	            return reg.test(value);  
	        },  
	        message: '邮编必须是非0开始的6位数字.'  
	    },
	    //用户账号验证(只能包括 _ 数字 字母)
	    account: {//param的值为[]中值  
	        validator: function (value, param) {  
	            if (value.length < param[0] || value.length > param[1]) {  
	                $.fn.validatebox.defaults.rules.account.message = '用户名长度必须在' + param[0] + '至' + param[1] + '范围';  
	                return false;  
	            } else {  
	                if (!/^[\w]+$/.test(value)) {  
	                    $.fn.validatebox.defaults.rules.account.message = '用户名只能数字、字母、下划线组成.';  
	                    return false;  
	                } else {  
	                    return true;  
	                }  
	            }  
	        }, 
	        message: '账号只能由数字和字母组成'  
	    },
	    //身份证的验证
	    checkCard:{
	    	validator: function(value){
	    		//15位身份证
	    		var reg1 = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;
	    		//18位身份证
	    		var reg2 = /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[A-Z])$/;
	    		if(reg1.test(value) || reg2.test(value)){
	    			return true;
	    		}else{
		    		$.fn.validatebox.defaults.rules.account.message = '身份验证失败';
					return false;
				    }
	    	},
	    	message:'不是有效的身份证号'
	    },
	    //验证组织机构代码
	    checkorganizationCode: {
	    	validator: function(value){
	    		 	var ws = [3, 7, 9, 10, 5, 8, 4, 2];
	    		    var str = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ';
	    		    var reg = /^([0-9A-Z]){8}-[0-9|X]$/;// /^[A-Za-z0-9]{8}-[A-Za-z0-9]{1}$/
	    		    var sum = 0;
	    		    for (var i = 0; i < 8; i++){
	    		        sum += str.indexOf(value.charAt(i)) * ws[i];
	    		    }
	    		    var c9 = 11 - (sum % 11);
	    		    c9 = c9 == 10 ? 'X' : c9
	    		    if (!reg.test(value) || c9 == value.charAt(9)) {			    		    	
	    		        $(this).focus();
	    		        return false;
	    		    }else{
	    		    	return true;
	    		    }
	    	},
	    	message:'不是有效的组织机构代码！'
	    },
	    //验证WSDL
	    checkWSDL: {     
            validator: function(value,param){               
                 var reg = "^(http://|([0-9]{1,3}[.]{1}[0-9]{1,3}[.]{1}[0-9]{1,3}[.]{1}[0-9]{1,3}:[0-9]{1,4}))[/a-zA-Z0-9._%&:=(),?+]*[?]{1}wsdl$";  
                 return reg.test(value);  
            },     
            message: '请输入合法的WSDL地址'     
        },
        //验证IP地址
        checkIp : {
            validator : function(value) {  
                var reg = /^((1?\d?\d|(2([0-4]\d|5[0-5])))\.){3}(1?\d?\d|(2([0-4]\d|5[0-5])))$/ ;  
                return reg.test(value);  
            },  
            message : 'IP地址格式不正确'  
        },
        //下拉框必填
        selectValueRequired: {   
            validator: function(value,param){             
                 if (value == "" || value.indexOf('请选择') >= 0) {   
                    return false;  
                 }else {  
                    return true;  
                 }    
            },   
            message: '该下拉框为必选项'   
        }   
	});
};
