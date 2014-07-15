function startFx(c, stopFunction){

    var lastTime = 0;
    var vendors = ['ms', 'moz', 'webkit', 'o'];
    for(var x = 0; x < vendors.length && !window.requestAnimationFrame; ++x) {
        window.requestAnimationFrame = window[vendors[x]+'RequestAnimationFrame'];
        window.cancelAnimationFrame = window[vendors[x]+'CancelAnimationFrame']
                                   || window[vendors[x]+'CancelRequestAnimationFrame'];
    }
 
    if (!window.requestAnimationFrame)
        window.requestAnimationFrame = function(callback, element) {
            var currTime = new Date().getTime();
            var timeToCall = Math.max(0, 16 - (currTime - lastTime));
            var id = window.setTimeout(function() { callback(currTime + timeToCall); },
              timeToCall);
            lastTime = currTime + timeToCall;
            return id;
        };
 
    if (!window.cancelAnimationFrame)
        window.cancelAnimationFrame = function(id) {
            clearTimeout(id);
        };
		
		drawCanvas(c,stopFunction);
};
  
  
rand = function(n){
	return Math.floor(Math.random()*n);
};
	
function drawCanvas(canvas,stopFunction) {
	var ctx = canvas.getContext("2d");
	canvas.width=600;
	canvas.height=600;
	var w = ctx.width = canvas.width;
	var h = ctx.height = canvas.height;

	var columns = 10;
	var lines = 10;

	//var size = w/columns;
	var walkers = [];
	var nwalkers = 8;
	for (i=0; i<nwalkers; i++) walkers[i] = rand(columns*lines);
	
	var states = [];
	var nstates = 4;
	for(i=0;i<columns;i++) {
		for(j=0;j<lines;j++) {
			states[i+j*columns] = rand(nstates);
		}
	}
	
	var renderToCanvas = function (width, height, renderFunction) {
		var buffer = document.createElement('canvas');
		buffer.width = width;
		buffer.height = height;
		renderFunction(buffer.getContext('2d'));
		return buffer;
	};

	function drawThis() {
		
		var d = new Date();
		var timer = d.getTime();
		
		var cached = renderToCanvas(w,h, function (ctx) {
		
			columns = 10;
			lines = 10;
			
			var colwidth = w/columns;
			var linspace = h/lines;
			size = colwidth;
			
			color2 = "rgba(255,0,0,1.0)";
			
			ctx.strokeStyle = color2;
			ctx.fillStyle = color2;
			ctx.lineCap = 'round';
			ctx.globalCompositeOperation = 'xor';
			ctx.shadowOffsetX = 5;
			ctx.shadowOffsetY = 6;
			ctx.shadowColor = 'rgba(0,0,0,0.1)';
			
			//var lW = Math.sin(timer/1000)*(Math.sin(timer/2000)+1.0)*50 + (Math.sin(timer/3000)+1.0)*50;
			lW = Math.sin(timer/1000)*30+32;
			
			for(i=0;i<columns;i++) {
				for(j=0;j<lines;j++) {
					var floatingx = colwidth*i;
					var floatingy = linspace*j;
					var halfsize = size*.5;
					switch(states[i+j*columns]) {
						case 0:
							ctx.strokeStyle = "rgba(255,0,0,1)";
							ctx.lineWidth = lW;
							ctx.beginPath();
							ctx.moveTo(floatingx-halfsize, floatingy-halfsize);	
							ctx.lineTo(floatingx+halfsize, floatingy+halfsize);
							ctx.stroke();
						break;
						case 1:
							ctx.strokeStyle = "rgba(255,0,0,1)";
							ctx.lineWidth = lW;
							ctx.beginPath();
							ctx.moveTo(floatingx+halfsize, floatingy-halfsize);	
							ctx.lineTo(floatingx-halfsize, floatingy+halfsize);
							ctx.stroke();
						break;
						case 2:
							ctx.strokeStyle = "rgba(0,0,200,1)";
							ctx.lineWidth = lW;
							ctx.beginPath();
							ctx.moveTo(floatingx-halfsize, floatingy-halfsize);	
							ctx.lineTo(floatingx+halfsize, floatingy+halfsize);
							ctx.stroke();
						break;
						case 3:
							ctx.strokeStyle = "rgba(0,0,200,1)";
							ctx.lineWidth = lW;
							ctx.beginPath();
							ctx.moveTo(floatingx-halfsize, floatingy-halfsize);	
							ctx.lineTo(floatingx+halfsize, floatingy+halfsize);
							ctx.stroke();
						break;
					}
				}
			}
			
		});

		ctx.clearRect(0,0,w,h);

		ctx.drawImage(cached, 0, 0);
	}
	
	(loop = function() {
		if(!stopFunction()){
			requestAnimationFrame(loop);
			drawThis();
		}
		//setTimeout(loop,20);
	})();
	
	(walk = function() {
		walkers[0]++;
		console.log(walkers.length);
		for(var i=0; i<walkers.length; i++) {
			switch(rand(4)) {
				case 0:
					walkers[i] += columns;
				break;
				case 1:
					walkers[i] -= columns;
				break;
				case 2:
					walkers[i]++;
				break;
				case 3:
					walkers[i]--;
				break;
			}
			
			if (walkers[i]>columns*lines) walkers[i] -= columns*lines;
			if (walkers[i]<0) walkers[i] += columns*lines;

			states[walkers[i]] = rand(nstates);
		}

		if(!stopFunction()){
			setTimeout(walk,200);
		}
	})();

}