(function($) {
	jQuery.comboCache = {};
	$.extend(jQuery.comboCache, {
		map : {},
		push : function(key, value) {
			jQuery.comboCache.map[key] = value;
		},
		remove : function(key) {
			delete (jQuery.comboCache.map[key]);
		},
		clear : function() {
			jQuery.comboCache.map = {};
		},
		get : function(key) {
			return jQuery.comboCache.map[key];
		}
	});
})(jQuery);
