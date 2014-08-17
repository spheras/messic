function startFx(canvas,stopFunction){

	var ctx = canvas.getContext("2d");
	var w = ctx.width=canvas.width;
	var h = ctx.height=canvas.height;
	
	//ctx.shadowOffsetX = 5;
	//	ctx.shadowOffsetY = 5;
	//	ctx.shadowColor = 'rgba(0,0,0,0.2)';
	
	var num = 200;
	var angle = (Math.PI*2)/num;
	var size = 90;
	var opening, phase1, phase2;

	function drawThis() {
		var d = new Date();
		var timer = d.getTime();
		
		color = "rgba(0,0,200,0.05)";
		ctx.fillStyle = color;
		
		phase1 = timer/10000;
		phase2 = timer/1000;
		
		for (var i=0; i<num; i++) {
			
				opening = 120+Math.sin(i*angle)*50;
				
				//size = 40+Math.sin(timer/1000)*10+Math.sin(timer*(j-numy*.5)/1000)*10;
				ctx.save();
				ctx.translate( w*.5+Math.sin(i*angle+phase1)*opening, h*.5+Math.cos(i*angle+phase1)*opening );
				ctx.rotate(i*angle+Math.sin(phase2+Math.sin(i*angle)));
				ctx.beginPath();
				ctx.moveTo(-size*.5,-size*.5);
				ctx.lineTo(0,size);
				ctx.lineTo(size*.5,-size*.5);
				//ctx.lineTo(size*.5,size/2*Math.sqrt(3));
				ctx.fill();
				ctx.closePath();

				ctx.restore();
		}
	}
	
	(loop = function() {
		if(!stopFunction()){
			ctx.clearRect(0,0,w,h);
			drawThis();
			setTimeout(loop,20);
		}
	})()
}