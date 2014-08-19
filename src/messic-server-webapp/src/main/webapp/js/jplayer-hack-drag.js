/*!
 * jPlayer Draggable Seekbar
 * http://wray.pro/
 *
 * Copyright 2013 Sam Wray
 * Released under the MIT license
 * https://github.com/2xAA/jplayer-draggable-seekbar/blob/master/LICENSE
 */

/* Magic touch->mouse function (Not written by me) */
function JPlayerHackDrag_touchHandler(event) {
	var touch = event.changedTouches[0];

	var simulatedEvent = document.createEvent("MouseEvent");

	simulatedEvent.initMouseEvent({
		touchstart : "mousedown",
		touchmove : "mousemove",
		touchend : "mouseup"
	}[event.type], true, true, window, 1, touch.screenX, touch.screenY,
			touch.clientX, touch.clientY, false, false, false, false, 0, null);

	touch.target.dispatchEvent(simulatedEvent);
	event.preventDefault();
}

function JPlayerHackDrag_addListeners() {
	/* Hide Chrome's annoying text-select-on-drag cursor */
	document.onselectstart = function() {
		return false;
	}

	/* Bind touch events to the magical script up there */
	document.getElementById('jp-seek-bar').addEventListener("touchstart",
			JPlayerHackDrag_touchHandler, true);
	document.getElementById('jp-seek-bar').addEventListener("touchmove",
			JPlayerHackDrag_touchHandler, true);
	document.getElementById('jp-seek-bar').addEventListener("touchend",
			JPlayerHackDrag_touchHandler, true);
	document.getElementById('jp-seek-bar').addEventListener("touchcancel",
			JPlayerHackDrag_touchHandler, true);

	/* Bind mouse events */
	document.getElementById('jp-seek-bar').addEventListener('mousedown',
			JPlayerHackDrag_mouseDown, false);
	document.getElementById('jp-seek-bar').addEventListener('mousedown',
			JPlayerHackDrag_divMove, true);
	window.addEventListener('mouseup', JPlayerHackDrag_mouseUp, false);

	$('.jp-volume-bar').mousedown(function() {
		var parentOffset = $(this).offset(), height = $(this).height();
		$(window).mousemove(function(e) {
			if (true || vertical) {
				var y = e.pageY - parentOffset.top, volume = 1-(y / height)
				if (volume > 1) {
					$("#jquery_jplayer").jPlayer("volume", 1);
				} else if (volume <= 0) {
					$("#jquery_jplayer").jPlayer("mute");
				} else {
					$("#jquery_jplayer").jPlayer("volume", volume);
					$("#jquery_jplayer").jPlayer("unmute");
				}
			} else {
				var x = e.pageX - parentOffset.left, volume = x / width
				if (volume > 1) {
					$("#jquery_jplayer").jPlayer("volume", 1);
				} else if (volume <= 0) {
					$("#jquery_jplayer").jPlayer("mute");
				} else {
					$("#jquery_jplayer").jPlayer("volume", volume);
					$("#jquery_jplayer").jPlayer("unmute");
				}
			}
		});
		return false;
	}).mouseup(function() {
		$(window).unbind("mousemove");
	});
}

function JPlayerHackDrag_mouseUp() {
	window.removeEventListener('mousemove', JPlayerHackDrag_divMove, true);
}

function JPlayerHackDrag_mouseDown(e) {
	window.addEventListener('mousemove', JPlayerHackDrag_divMove, true);
}

function JPlayerHackDrag_divMove(e) {
	var div = document.getElementById('jp-play-bar'); // Set this to the play
	// bar
	var container = document.getElementById('jp-seek-bar'); // Set this to the
	// seek
	// bar
	var offset = $(container).offset().left;
	var maxwidth = (container.offsetWidth);

	/* Percentage calc */
	if (e.clientX + offset <= maxwidth + offset * 2) {
		div.style.width = (e.clientX - offset) / (maxwidth / 100) + '%';
		$("#jquery_jplayer").jPlayer("playHead",
				(e.clientX - offset) / (maxwidth / 100));
	}
}
