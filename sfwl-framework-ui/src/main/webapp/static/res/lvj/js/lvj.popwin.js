
jQuery.fn.lvjOpenWin = function(_configJson){
	var myWinObj = this;
	if(_configJson.iconCls ==undefined){
		_configJson.iconCls = "icon-edit";
	}
	if(_configJson.modal ==undefined){
		_configJson.modal = true;
	}
	if(_configJson.closed ==undefined){
		_configJson.closed = true;
	}
	if(_configJson.collapsible ==undefined){
		_configJson.collapsible = false;
	}
	if(_configJson.minimizable ==undefined){
		_configJson.minimizable = false;
	}
	if(_configJson.maximizable ==undefined){
		_configJson.maximizable = false;
	}
	//设置默认参数
	if(_configJson != null){
		myWinObj.window(_configJson).window('open');
	}else{
		myWinObj.window({
			width:600,
			height:400,
			top:20,
            left:200,
			iconCls:'icon-edit',
			modal:true,
			closed:true,
			collapsible:false,
			minimizable:false,
			maximizable:false
		}).window('open');
	}
};