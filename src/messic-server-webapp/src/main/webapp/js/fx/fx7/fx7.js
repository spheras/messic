function startFx(canvas,stopFunction){
			var app = {
				pi: Math.PI,
				r: Math.random,
				m: Math,
				init: function(){
					var self = this;
					this.count = 0;

					this.$obj = $(canvas);
					
					


					this.ctx = this.$obj[0].getContext('2d');
					



			 		self.factor = 1;
			 		self.iter = self.osc(1,10,0.01);
			 		
			 		
			 		this.reset();
			 		$(window).bind('resize',function(){
			 			self.reset();
			 		});

				},
				reset: function(){
					var self = this;

					window.clearInterval(this.anim);

					this.$obj.height($(window).height());
					this.$obj.width($(window).width());

					this.$obj.attr('height',$(window).height());
					this.$obj.attr('width',$(window).width());

					self.width  = this.$obj.width();
			 		self.height = this.$obj.height();
			 		
			 		self.paths = [];
			 		for (var i = 0; i < 1200; i++) {
			 			if(i%50 == 1) {
			 				var x = this.r()*self.width;
			 				var y = this.r()*self.height;
			 			}
			 			self.paths.push(new Path(x, y));
			 		};
			 		

			 		this.anim = window.setInterval(this.draw,16)
				},
				clear: function(){
					var self = this;
					self.ctx.clearRect(0,0,self.width, self.height);
				},
				fade: function(){
					var self = this;
					this.ctx.fillStyle = "rgba(255,255,255,0.01)";
				    self.ctx.rect(0, 0, self.width, self.height);
				    self.ctx.fill();
				},
				draw: function(){
					if(stopFunction()){
						$(window).unbind('resize');
						window.clearInterval(app.anim);
						return;
					}
				
					//console.log('draw');
					var self = app;
					// self.fade();
					self.factor = self.iter();

					for (var i = 0; i < self.paths.length; i++) {
						self.paths[i].updatePosition();
					};
					
					
					
					
					
				},
				osc: function(low, high, inc) {

				    // basic test for illegal parameters
				    if (low > high || inc < 0 ||  2 * (high - low) < inc) 
				        return function() { return NaN; };

				    var curr = low;
				    return function() {
				        var ret = curr;
				        curr += inc;

				        if (curr > high || curr < low) 
				        {   
				            curr = inc > 0 ? 2 * high - curr : 2 * low - curr;  
				            inc = -inc;
				        };

				        return app.roundNumber(ret,2);
				    }; 
				},
				
				roundNumber:function(number, decimals) { // Arguments: number to round, number of decimal places

			        if (!decimals) {decimals = 0;};

			        var newnumber = new Number(number+'').toFixed(parseInt(decimals));
			        return  parseFloat(newnumber); 
			    }
			}

			function Path(startX, startY){
				
				// private vars
				
				var a 			= app;
				var ctx 		= a.$obj[0].getContext('2d');
				var that 		= this;

				var opacity		= a.roundNumber(a.r() * 0.5,1);
				var red 		= a.r() * 256 << 0;
				// var green 		= a.r() * 256 << 0;
				// var blue 		= a.r() * 256 << 0;
				var green 		= 0;
				var blue 		= 0;
				

				// var startX 		= a.r() * startX*2;
				// var startY 		= a.r() * startY*2;

				var width		= a.roundNumber(a.r() * 2,2);
				var lastaction 	= 'arc';
				var angle  		= a.r()*2 * Math.PI; // start angle in rads
				var distance 	= 0;
				var direction 	= 1;
				var lastdata	= {};

				
				ctx.save();
				this.updatePosition = function(){
					lastaction = (lastaction == 'arc') ? 'line' : 'arc';
					direction = (direction > 0) ? -1 : 1;
					switch (lastaction){
						case 'arc':
							
							
							
							// angle = this.getAngle();

							// // need to work out an angle here
							// var coords = {
							// 	x : dist * Math.cos(angle),
       //     						y : dist * Math.sin(angle)	
							// };
							// ctx.beginPath();
							// ctx.moveTo(startX,startY);
							// ctx.strokeStyle = "rgba("+red+","+green+","+blue+","+opacity+")";
							// ctx.lineWidth = width;			
							
							// ctx.arcTo(startX, startY, startX+coords.x, startY+coords.y, dist);
							// ctx.stroke();

							// startX = startX+coords.x;
							// startY = startY+coords.y;

								


						case 'line':
							dist = app.r()*12 << 0; // bitshift floor magic!
							
							angle = a.r()*360 << 0;
							angle = this.getAngle();

							// need to work out an angle here
							var coords = {
								x : dist * Math.cos(angle),
           						y : dist * Math.sin(angle)	
							};
           					ctx.beginPath();
							ctx.moveTo(startX,startY);
							ctx.strokeStyle = "rgba("+red+","+green+","+blue+","+opacity+")";
							ctx.lineWidth = width;			
							ctx.lineTo(startX+coords.x, startY+coords.y);
							ctx.stroke();
							
							distance += dist;
							
							lastdata.coords = coords;
							lastdata.angle = angle;

							startX = startX+coords.x;
							startY = startY+coords.y;
							break;


					}

					return lastaction;
				}

				this.getAngle = function(){
					return direction * (a.roundNumber( a.r()*2, 2 ) * Math.PI);
				}


				// is this needed?
				return that;


			}
			
			app.init();
}