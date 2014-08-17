function startFx(canvas,stopFunction){
	var ctx = canvas.getContext("2d");
	var w = ctx.width = canvas.width;
	var h = ctx.height = canvas.height;
	
	//ctx.shadowOffsetX = 5;
	//	ctx.shadowOffsetY = 5;
	//	ctx.shadowColor = 'rgba(0,0,0,0.2)';
	
	var num = 58;
	var angle = (Math.PI*2)/num;
	var size = 70;
	var opening, phase1, phase2, pointx, pointy, pointx2, pointy2, pointx3, pointy3;

	function drawThis() {
		var d = new Date();
		var timer = d.getTime();
		
		color = "rgba(0,200,0,0.1)";
		ctx.fillStyle = color;
		
		phase1 = timer/10000;
		phase2 = (timer+1000)/10000;
		
		for (var i=0; i<num; i++) {
			
			for (opening = 60; opening < 1000; opening = opening*2) {
				//opening = 260;
				
				pointx = w*.5+Math.sin(i*angle+phase1)*opening;
				pointy = h*.5+Math.cos(i*angle+phase1)*opening;

				pointx2 = w*.5+Math.sin(i*angle+phase2)*opening*.75;
				pointy2 = h*.5+Math.cos(i*angle+phase2)*opening*.75;

				pointx3 = w*.5+Math.sin(i*angle+phase1)*opening*.5;
				pointy3 = h*.5+Math.cos(i*angle+phase1)*opening*.5;
				
				ctx.beginPath();
				ctx.moveTo(pointx,pointy);
				ctx.lineTo(pointx2,pointy2);
				ctx.lineTo(pointx3,pointy3);
				ctx.fill();
				ctx.closePath();
			}
			
			for (opening = 90+Math.sin(phase1)*50; opening < 1000; opening = opening*2) {
				
				pointx = w*.5+Math.sin(i*angle)*opening;
				pointy = h*.5+Math.cos(i*angle)*opening;

				pointx2 = w*.5+Math.sin(i*angle-0.1)*opening*.75;
				pointy2 = h*.5+Math.cos(i*angle-0.1)*opening*.75;

				pointx3 = w*.5+Math.sin(i*angle)*opening*.5;
				pointy3 = h*.5+Math.cos(i*angle)*opening*.5;
				
				ctx.beginPath();
				ctx.moveTo(pointx,pointy);
				ctx.lineTo(pointx2,pointy2);
				ctx.lineTo(pointx3,pointy3);
				ctx.fill();
				ctx.closePath();
			}


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
