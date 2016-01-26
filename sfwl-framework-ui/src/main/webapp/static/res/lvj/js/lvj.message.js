/**
 * Description: 消息提示公用类
 * 
 * 修改记录:
 * 修改后版本			修改人   			修改日期   		修改内容
 * 2015-10-12		Jeffery   		2015-10-12		create
 * 
 * 
 * jQuery.lvjMsg.success()	-	操作成功
 * jQuery.lvjMsg.error(errMsg)	-	操作失败提示
 * jQuery.lvjMsg.selectNoData()	-	没有选中数据提示
 * jQuery.lvjMsg.saveNoData()	-	没有数据需要更新
 * jQuery.lvjMsg.selectOne()	-	请选择一条数据
 * jQuery.lvjMsg.info(infoMsg)	-	消息提示
 * jQuery.lvjMsg.repeatEnable()	-	重复启用提示
 * jQuery.lvjMsg.repeatDisabled()	-	重复禁用提示
 * jQuery.lvjMsg.cannotEnable()	-	有数据未保存，无法启用数据
 * jQuery.lvjMsg.cannotDisabled()	-	数据未启用无法禁用
 */
jQuery.lvjMsg = new function(){
	var MESSAGE_TYPE_INFO = "info";
	var MESSAGE_TYPE_ERROR = "error";
	var MESSAGE_OPERATION_TIPS = "操作提示";
	var MESSAGE_OPERATION_SUCCESS = "操作成功！";
	var MESSAGE_SELECT_NO_DATA = "没有选中数据。";
	var MESSAGE_SAVE_NO_DATA = "没有数据需要更新。";
	var MESSAGE_SELECT_ONLY_ONE = "请选择一条数据。";
	var MESSAGE_REPEAT_ENABLE = "数据已启用，无需重复启用！";
	var MESSAGE_REPEAT_DISABLED = "数据已禁用，无需重复禁用！";
	var MESSAGE_CANNOT_ENABLE = "有数据未保存，无法启用数据！";
	var MESSAGE_CANNOT_DISABLED = "数据未启用，无法禁用！";
	
	return {
		
		success : function(){
			$.messager.alert(MESSAGE_OPERATION_TIPS, MESSAGE_OPERATION_SUCCESS, MESSAGE_TYPE_INFO);
		},
	
		error : function(errMsg){
			$.messager.alert(MESSAGE_OPERATION_TIPS, errMsg, MESSAGE_TYPE_ERROR);
		},
		
		selectNoData : function(){
			$.messager.alert(MESSAGE_OPERATION_TIPS, MESSAGE_SELECT_NO_DATA, MESSAGE_TYPE_ERROR);
		},
		
		saveNoData : function(){
			$.messager.alert(MESSAGE_OPERATION_TIPS, MESSAGE_SAVE_NO_DATA, MESSAGE_TYPE_ERROR);
		},
		
		selectOne : function(){
			$.messager.alert(MESSAGE_OPERATION_TIPS, MESSAGE_SELECT_ONLY_ONE, MESSAGE_TYPE_ERROR);
		},
		
		info : function(infoMsg){
			$.messager.alert(MESSAGE_OPERATION_TIPS, infoMsg, MESSAGE_TYPE_INFO);
		},
		
		repeatEnable : function(){
			$.messager.alert(MESSAGE_OPERATION_TIPS, MESSAGE_REPEAT_ENABLE, MESSAGE_TYPE_ERROR);
		},
		
		repeatDisabled : function(){
			$.messager.alert(MESSAGE_OPERATION_TIPS, MESSAGE_REPEAT_DISABLED, MESSAGE_TYPE_ERROR);
		},
		
		cannotEnable : function(){
			$.messager.alert(MESSAGE_OPERATION_TIPS, MESSAGE_CANNOT_ENABLE, MESSAGE_TYPE_ERROR);
		},
		
		cannotDisabled : function(){
			$.messager.alert(MESSAGE_OPERATION_TIPS, MESSAGE_CANNOT_DISABLED, MESSAGE_TYPE_ERROR);
		}
		
	};
}