<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
//子页面中引入该JSP
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<base href="<%=basePath%>" />
<link href="<%=basePath%>/static/res/easyui/themes/metro/easyui.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>/static/res/easyui/themes/icon.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>/static/res/lvj/css/lvj.form.css" rel="stylesheet" type="text/css" />

<!-- by harry -->
<link href="<%=basePath%>/static/res/lvj/css/base.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>/static/res/lvj/css/form.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>/static/res/lvj/css/layout.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>/static/res/lvj/css/override.css" rel="stylesheet" type="text/css" />
<!-- h -->

<script src="<%=basePath%>/static/res/easyui/jquery.min.js" type="text/javascript"></script>
<script src="<%=basePath%>/static/res/easyui/jquery.easyui.min.js" type="text/javascript"></script>
<script src="<%=basePath%>/static/res/easyui/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>
<script src="<%=basePath%>/static/res/easyui/jquery.json.js" type="text/javascript"></script>
<script src="<%=basePath%>/static/res/lvj/js/lvj.jquery.util.js" type="text/javascript"></script>
<script src="<%=basePath%>/static/res/lvj/js/lvj.datagrid.js" type="text/javascript"></script>
<script src="<%=basePath%>/static/res/lvj/js/lvj.dateformat.js" type="text/javascript"></script>
<script src="<%=basePath%>/static/res/lvj/js/lvj.ajax.js" type="text/javascript"></script>
<script src="<%=basePath%>/static/res/lvj/js/lvj.cache.js" type="text/javascript"></script>
<script src="<%=basePath%>/static/res/lvj/js/lvj.tab.js" type="text/javascript"></script>
<script src="<%=basePath%>/static/res/lvj/js/lvj.message.js" type="text/javascript"></script>
<script src="<%=basePath%>/static/res/lvj/js/lvj.popwin.js" type="text/javascript"></script>
<script src="<%=basePath%>/static/res/lvj/js/lvj.business.js" type="text/javascript"></script>
<script type="text/javascript">
modulePath = "<%=basePath%>";
// 加载页面下拉框数据
$().ready(function(){
	$('input[class="lvj-combobox"]').each(function(){
	    var $this=$(this);
	    var comboKey = $this.attr('comboKey');
	    var data = parent.getComboByKey(comboKey);// 从缓存中获取数据
	    if(!data){
	    	jQuery.lvjAjax(modulePath + "/comCodeMaster/getCodeMasterByType.shtml",
	    			{"comboKey":comboKey},function(result){
	    		parent.setComboData(comboKey,result);// 将数据放入缓存中
	    		$this.combobox({
		    		data:result
		    	});
	    	});
	    }else{
	    	$this.combobox({
	    		data:data
	    	});
	    }
	});
});

</script>
