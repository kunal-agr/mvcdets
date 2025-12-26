/* * Custom Script for Daily Expense Tracker
 * Handles Sidebar toggles, Datepicker, and Panel collapses
 */

$('#calendar').datepicker({
});

!function ($) {
    // This handles the plus/minus icon toggle in the sidebar
    $(document).on("click", "ul.nav li.parent > a", function () {
        // Find the specific 'em' inside the 'icon' span to toggle
        $(this).find('span.icon em').toggleClass("fa-minus");
    });
    
    // Set initial state for sidebar icons
    $(".sidebar span.icon").find('em:first').addClass("fa-plus");
}(window.jQuery);

// Handle sidebar visibility on window resize
$(window).on('resize', function () {
    if ($(window).width() > 768) {
        $('#sidebar-collapse').collapse('show');
    }
});

$(window).on('resize', function () {
    if ($(window).width() <= 767) {
        $('#sidebar-collapse').collapse('hide');
    }
});

// Panel Collapse/Expand logic for dashboard panels
$(document).on('click', '.panel-heading span.clickable', function (e) {
    var $this = $(this);
    if (!$this.hasClass('panel-collapsed')) {
        $this.parents('.panel').find('.panel-body').slideUp();
        $this.addClass('panel-collapsed');
        $this.find('em').removeClass('fa-toggle-up').addClass('fa-toggle-down');
    } else {
        $this.parents('.panel').find('.panel-body').slideDown();
        $this.removeClass('panel-collapsed');
        $this.find('em').removeClass('fa-toggle-down').addClass('fa-toggle-up');
    }
});