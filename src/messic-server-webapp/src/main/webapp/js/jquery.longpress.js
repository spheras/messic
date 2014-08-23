/**
 * Longpress is a jQuery plugin that makes it easy to support long press
 * events on mobile devices and desktop borwsers.
 *
 * @name longpress
 * @version 0.1.2
 * @requires jQuery v1.2.3+
 * @author Vaidik Kapoor
 * @license MIT License - http://www.opensource.org/licenses/mit-license.php
 *
 * For usage and examples, check out the README at:
 * http://github.com/vaidik/jquery-longpress/
 *
 * Copyright (c) 2008-2013, Vaidik Kapoor (kapoor [*dot*] vaidik -[at]- gmail [*dot*] com)
 */
(function ($) {
    $.fn.longpress = function (longCallback, shortCallback, duration) {
        var touch = false;
        var click = false;
        if (typeof duration === "undefined") {
            duration = 500;
        }
        return this.each(function () {
            var $this = $(this);
            // to keep track of how long something was pressed
            var mouse_down_time;
            var timeout;

            // mousedown or touchstart callback
            function down_callback(e) {
                //messic, preventing defaults operations and propagations is important, maybe should be optional
                e.preventDefault();
                e.stopPropagation();

                mouse_down_time = new Date().getTime();
                var context = $(this);
                if (!touch) {

                    // set a timeout to call the longpress callback when time elapses
                    timeout = setTimeout(function () {
                        if (typeof longCallback === "function") {
                            longCallback.call(context, e);
                        } else {
                            $.error('Callback required for long press. You provided: ' + typeof longCallback);
                        }
                    }, duration);
                }
            }
            // mouseup or touchend callback
            function up_callback(e) {
                //messic, preventing defaults operations and propagations is important, maybe should be optional
                e.preventDefault();
                e.stopPropagation();

                var press_time = new Date().getTime() - mouse_down_time;
                if (press_time < duration || touch) {
                    // cancel the timeout
                    clearTimeout(timeout);
                    // call the shortCallback if provided
                    if (typeof shortCallback === "function") {
                        shortCallback.call($(this), e);
                    } else if (typeof shortCallback === "undefined") {;
                    } else {
                        $.error('Optional callback for short press should be a function.');
                    }
                }
            }
            // cancel long press event if the finger or mouse was moved
            function move_callback(e) {
                //messic, seems better to allow small movements
                //clearTimeout(timeout);
            }

            function click_callback(e) {
                //messic, preventing defaults operations and propagations is important, maybe should be optional
                e.preventDefault();
                e.stopPropagation();
            }

            function mousedown(e) {
                if (!touch) {
                    click = true;
                    down_callback(e);
                }
            }

            function mouseup(e) {
                if (!touch) {
                    click = true;
                    up_callback(e);
                }
            }

            function touchdown(e) {
                if (!click) {
                    touch = true;
                    down_callback(e);
                }
            }

            function touchup(e) {
                if (!click) {
                    touch = true;
                    up_callback(e);
                }
            }

            // Browser Support

            // Mobile Support
            $this.on('touchstart', touchdown);
            $this.on('touchend', touchup);
            $this.on('touchmove', move_callback);

            $this.on('mousedown', mousedown);
            $this.on('mouseup', mouseup);
            $this.on('click', click_callback);
            $this.on('mousemove', move_callback);

        });
    };
}(jQuery));