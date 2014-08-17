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
	var opening, phase1, phase2;

	function drawThis() {
		var d = new Date();
		var timer = d.getTime();
		
		color = "rgba(0,0,200,0.05)";
		ctx.fillStyle = color;
		
		phase1 = timer/10000;
		phase0 = (10000 - phase1);
		phase2 = timer/1000;
		
		for (var i=0; i<num; i++) {
			
				opening = 260;//120+Math.sin(i*angle)*50;
				
				var pointx = w*.5+Math.sin(i*angle+phase1)*opening;
				var pointy = h*.5+Math.cos(i*angle+phase1)*opening;

				var pointx2 = w*.5+Math.sin(i*angle+phase1)*opening*(.5+Math.sin(angle+phase2)*.5)*.5;
				var pointy2 = h*.5+Math.cos(i*angle+phase1)*opening*(.5+Math.sin(angle+phase2)*.5)*.5;

				var pointx3 = w*.5+Math.sin(i*angle+phase1+num)*opening*(.5+Math.sin(angle+phase1)*.5)*.5;
				var pointy3 = h*.5+Math.cos(i*angle+phase1+num)*opening*(.5+Math.sin(angle+phase1)*.5)*.5;
				
				var pointx4 = w*.5+Math.sin((num-i)*angle+phase0)*opening;
				var pointy4 = h*.5+Math.cos((num-i)*angle+phase0)*opening;

				var pointx5 = w*.5+Math.sin((num-i)*angle+phase0)*opening*(.5+Math.sin(angle+phase2)*.5)*.5;
				var pointy5 = h*.5+Math.cos((num-i)*angle+phase0)*opening*(.5+Math.sin(angle+phase2)*.5)*.5;

				var pointx6 = w*.5+Math.sin((num-i)*angle+phase0+num)*opening*(.5+Math.sin(angle+phase2+phase1)*.5)*.5;
				var pointy6 = h*.5+Math.cos((num-i)*angle+phase0+num)*opening*(.5+Math.sin(angle+phase2+phase1)*.5)*.5;

				ctx.beginPath();
				ctx.moveTo(pointx,pointy);
				ctx.lineTo(pointx2,pointy2);
				ctx.lineTo(pointx3,pointy3);
				ctx.fill();
				ctx.closePath();

				ctx.beginPath();
				ctx.moveTo(pointx4,pointy4);
				ctx.lineTo(pointx5,pointy5);
				ctx.lineTo(pointx6,pointy6);
				ctx.fill();
				ctx.closePath();
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