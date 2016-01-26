/**
 * Description: Tab页签公用类
 * 
 * 修改记录:
 * 修改后版本			修改人   			修改日期   		修改内容
 * 2015-09-29		Jeffery   		2015-09-29		create
 * 
 */
jQuery.lvjTab = new function(){
	return {
		// 默认打开选择的
		init : function(){
			$('.easyui-accordion li a').click(function() {
				var tabTitle = $(this).text();
				var url = $(this).attr("name");
				jQuery.lvjTab.addTab(tabTitle, url);
				$('.easyui-accordion li div').removeClass("selected");
				$(this).parent().addClass("selected");
			}).hover(function() {
				$(this).parent().addClass("hover");
			}, function() {
				$(this).parent().removeClass("hover");
			});
		},
		addTab : function(subtitle, url){
			if (!$('#tabs').tabs('exists', subtitle)) {
				$('#tabs').tabs('add', {
					title : subtitle,
					content : jQuery.lvjTab.createFrame(url),
//					href : url,
					closable : true,
					width : $('#mainPanel').width() - 10,
					height : $('#mainPanel').height() - 26
				});
			} else {
				$('#tabs').tabs('select', subtitle);
				var tab = $('#tabs').tabs('getSelected');
				$("#tabs").tabs('update', {
					tab : tab,
					options : {
						title : subtitle,
						style : { padding : '1px' },
						content : jQuery.lvjTab.createFrame(url),
						closable : true
					}
				});
			}
			jQuery.lvjTab.tabClose();
		},
		createFrame : function(url){
			var s = '<iframe name="mainFrame" scrolling="auto" frameborder="0"  src="'
				+ url
				+ '" style="width:100%;height:100%;"></iframe>';
			return s;
		},
		tabClose : function(){
			/*双击关闭TAB选项卡*/
			$(".tabs-inner").dblclick(function() {
				var subtitle = $(this).children("span").text();
				$('#tabs').tabs('close', subtitle);
			})

			$(".tabs-inner").bind('contextmenu', function(e) {
				$('#mm').menu('show', {
					left : e.pageX,
					top : e.pageY,
				});
				var subtitle = $(this).children("span").text();
				$('#mm').data("currtab", subtitle);
				return false;
			});
		},
		closeTab : function(tabName){
			$('#tabs').tabs('close', tabName);
		},
		tabCloseEven : function(){
			//关闭当前
			$('#mm-tabclose').click(function() {
				var currtab_title = $('#mm').data("currtab");
				$('#tabs').tabs('close', currtab_title);
			})
			//全部关闭
			$('#mm-tabcloseall').click(function() {
				$('.tabs-inner span').each(function(i, n) {
					var t = $(n).text();
					$('#tabs').tabs('close', t);
				});
			});

			//关闭除当前之外的TAB
			$('#mm-tabcloseother').click(function() {
				var currtab_title = $('#mm').data("currtab");
				$('.tabs-inner span').each(function(i, n) {
					var t = $(n).text();
					if (t != currtab_title)
						$('#tabs').tabs('close', t);
				});
			});
			//关闭当前右侧的TAB
			$('#mm-tabcloseright').click(function() {
				var nextall = $('.tabs-selected').nextAll();
				if (nextall.length == 0) {
					//msgShow('系统提示','后边没有啦~~','error');
					alert('后边没有啦~~');
					return false;
				}
				nextall.each(function(i, n) {
					var t = $('a:eq(0) span', $(n)).text();
					$('#tabs').tabs('close', t);
				});
				return false;
			});
			//关闭当前左侧的TAB
			$('#mm-tabcloseleft').click(function() {
				var prevall = $('.tabs-selected').prevAll();
				if (prevall.length == 0) {
					alert('到头了，前边没有啦~~');
					return false;
				}
				prevall.each(function(i, n) {
					var t = $('a:eq(0) span', $(n)).text();
					$('#tabs').tabs('close', t);
				});
				return false;
			});

			//退出
			$("#mm-exit").click(function() {
				$('#mm').menu('hide');

			})
		}
	};
};