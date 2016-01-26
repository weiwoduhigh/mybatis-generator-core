/**
 * jQuery Ajax 封装
 */
jQuery.lvjAjax = function(url, data, successfn, errorfn){
	url = (url==null || url=="" || typeof(url)=="undefined")? "" : url;
	$.ajax({
		type : "POST",
		dataType : "json",
		url : url,
		data : data,
		success : function(result) {
			if(result){
				if(typeof(successfn) == "undefined"){
					if(result.success){// 操作成功
						jQuery.lvjMsg.success();
					}else{// 操作失败
						if(typeof(errorfn) == "undefined"){
							jQuery.lvjMsg.error(result.message);
						}else{
							errorfn(result);
						}
					}
				}else{
					successfn(result);
				}
			}else{// 无返回数据
				if(typeof(errorfn) == "undefined"){
					jQuery.lvjMsg.error("返回信息为空!");
				}else{
					errorfn(result);
				}
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			jQuery.lvjMsg.error('请求失败！\nstatus:'
					+ XMLHttpRequest.status + '; \nreadyState:'
					+ XMLHttpRequest.readyState + '; \ntextStatus:'
					+ textStatus);
		}
	});
};