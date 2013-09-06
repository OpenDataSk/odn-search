$(document).ready(function(){
  $(document).bind('click', function(e) {
		var $clicked = $(e.target);
		if (! $clicked.parents().hasClass("activedropdown"))
			$(".activedropdown").hide();
	});
  $(".select").click( function () { 
	  $(this).next("ul").addClass('activedropdown').toggle();
		return false;
	});
  $(".select_list li").click( function () {
	  $(this).siblings().removeClass("selected");
	  $(this).addClass("selected").parent().prev(".select").text($(this).text());
	  $(this).parent().nextAll("select").find("option").val($(this).text());
	  $(this).parent().nextAll("input").val($(this).text());
	  $(this).parent(".activedropdown").hide();
	});                                  
	$(".select_list li").hover(
	  function () { $(this).addClass("hover"); }, 
	  function () { $(this).removeClass("hover"); }
  );
});