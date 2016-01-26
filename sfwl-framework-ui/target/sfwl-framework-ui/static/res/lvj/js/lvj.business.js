// ROOT节点
var ROOT = "ROOT";
//启用&禁用
var ADD = "ADD";
var ENABLE = "ENABLE";
var DISABLED = "DISABLED";
// 是&否
var YES = "Y";
var NO = "N";
var yesOrNoJson = [{value:NO,text:'否'},{value:YES,text:'是'}];
/**
 * 启用停用格式化
 * @param flag
 * @returns {String}
 */
function recordFlagFormatter(flag){
	if(ADD == flag){
		return "新增";
	}else if(ENABLE == flag){
		return "启用";
	}else if(DISABLED == flag){
		return "禁用";
	}else{
		return "";
	}
}

/**
 * 启用数据验证
 */
function enableRecord(flag,enableFn){
	// 数据已启用，无需重复启用！
	if(recordFlagFormatter(ENABLE) == flag){
		jQuery.lvjMsg.repeatEnable();
		return;
	}
	if(recordFlagFormatter(ADD) == flag){
		// 启用新增数据，需要确认
		$.messager.confirm('启用数据','启用数据后无法删除该数据，是否启用？',function(r){
			if(r){
				enableFn();
			}
		});
	}else{
		enableFn();
	}
}
/**
 * 禁用数据验证
 */
function disabledRecord(flag,disabledFn){
	// 数据未启用，无法禁用！
	if(recordFlagFormatter(ADD) == flag){
		jQuery.lvjMsg.cannotDisabled();
		return;
	}
	// 数据已禁用，无需重复禁用！
	if(recordFlagFormatter(DISABLED) == flag){
		jQuery.lvjMsg.repeatDisabled();
		return;
	}
	disabledFn();
}

/**
 * 批量启用验证
 */
function batchEnable(selectRows,batchEnableFn){
	// 未选中数据
	if(selectRows == null || selectRows.length < 1){
		jQuery.lvjMsg.selectNoData();
		return ;
	}
	for(var i=0;i<selectRows.length;++i){
		// 新增的未进行保存的记录需要先保存再启用
		if(jQuery.lvjIsNull(selectRows[i].id)){
			jQuery.lvjMsg.cannotEnable();
			return;
		}
		// 启用的数据不需要重复启用
		if(ENABLE == selectRows[i].flag){
			jQuery.lvjMsg.repeatEnable();
			return;
		}
	}
	batchEnableFn();
}

/**
 * 批量禁用验证
 */
function batchDisabled(selectRows,batchDisabledFn){
	// 未选中数据
	if(selectRows == null || selectRows.length < 1){
		jQuery.lvjMsg.selectNoData();
		return ;
	}
	for(var i=0;i<selectRows.length;++i){
		if(jQuery.lvjIsNull(selectRows[i].id)){// 新增的未进行保存的记录需要先保存再启用
			jQuery.lvjMsg.cannotEnable();
			return;
		}
		if(ADD == selectRows[i].flag){// 数据未启用无法禁用
			jQuery.lvjMsg.cannotDisabled();
			return;
		}
		if(DISABLED == selectRows[i].flag){// 禁用的数据不需要重复禁用
			jQuery.lvjMsg.repeatDisabled();
			return;
		}
	}
	batchDisabledFn();
}

/**
 * 是否格式化
 */
function yesOrNoFormatter(flag){
	if(YES == flag){
		return "是";
	}else if(NO == flag){
		return "否";
	}else{
		return "";
	}
}

function getCodeMaster(comboKey){
	return parent.getComboByKey(comboKey);
}
/**
 * 格式化Grid参数
 * @param value
 * @param comboKey
 * @returns
 */
function codeMasterFormatter(value,comboKey){
	var data = parent.getComboByKey(comboKey);// 从缓存中获取数据
	for(var key in data){
		if(value == data[key].value){
			return data[key].text;
		}
	}
}

/**
 * 格式化用户
 * @param userName
 * @returns
 */
function userFormatter(userName){
	var ret = top.getUser(userName);
	if(!ret){
		return userName;
	}else{
		return ret;
	}
}