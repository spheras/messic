function startFx(canvas,stopFunction){		
			var app = {
				pi: Math.PI,
				r: Math.random,
				m: Math,
				numpaths: 55,
				init: function(){
					var self = this;
					this.count = 0;

					this.$obj = $(canvas);
					
					this.cycle = this.osc(1.2,2,0.2);
					this.ctx = this.$obj[0].getContext('2d');
			 		
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
			 		for (var i = 0; i < this.numpaths; i++) {
			 			
			 				var x = self.width/2;
			 				var y = self.height/2;
			 			
			 			self.paths.push(new bPath(x, y, i+1));
			 		};
			 		// this.animate();
					// window.setTimeout(app.draw,16)			 		
					
			 		this.anim = window.setInterval(this.draw,16)
			 		// this.anim = window.-webkitRequestAnimationFrame
				},
				animate: function(){
					app.draw();
					-webkitRequestAnimationFrame(app.animate);
				},
				clear: function(){
					var self = this;
					self.ctx.clearRect(0,0,self.width, self.height);
				},
				endAnim: function(){
					window.clearInterval(this.anim);
				},
				fade: function(){
					var self = this;
					this.ctx.fillStyle = "rgba(0,0,0,0.025)";
				    self.ctx.rect(0, 0, self.width, self.height);
				    self.ctx.fill();
				},
				draw: function(){
					if(stopFunction()){
						$(window).unbind('resize');
						window.clearInterval(app.anim);
						return;
					}
				
					var self = app;

					for (var i = 0; i < self.paths.length; i++) {
						
						self.paths[i].updatePosition();
					};
					
					self.fade();					
					
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

			function bPath(startX, startY, index) {

				var distance 	= 0
				var dist 		= 0;
				var a 			= app;

				
				this.direction	= 1;
				this.painting = {
					x: null,
					y: null,
					angle: null,
					angle_end:null
				};
				var ctx 		= a.$obj[0].getContext('2d');
				var that 		= this;

				var opacity		= a.roundNumber(a.r() * 0.3+0.5,1);
				var red 		= 128 + (a.r() * 128 << 0);
				var green		= 32; //128 + (a.r() * 32 << 0);
				var blue 		= 32; //128 + (a.r() * 32 << 0);
				var width 		= a.roundNumber(a.r() * 2,2);

				

				var lastaction 	= 'line'; // arc or line
				var angle 		= 0;
				var angle_end	= a.roundNumber( 360 / a.numpaths, 2 ) * Math.PI/180  * index;
				var coords = {
					x : null,
					y : null,
					x2 : startX,
					y2 : startY
				};

				// ctx.moveTo(startX, startY);
				// ctx.beginPath();
				
				this.swapAction = function(){
					lastaction = (lastaction == 'arc') ? 'line' : 'arc';
				}

				this.updatePosition = function(){
					
					if (distance > startY) {
						
						distance = 0;
						coords.x2 = startX;
						coords.y2 = startY;
						lastaction = 'line';
						red 		= 128 + (a.r() * 128 << 0);
						green		= 32 + (a.r() * 32 << 0);
						blue 		= 32 + (a.r() * 32 << 0);
						width 		= a.roundNumber(a.r() * 2,2);
						angle = 0;
					};

					

					switch (lastaction){

						case 'arc':
				
							ctx.beginPath();
							ctx.moveTo(coords.x,coords.y);
							ctx.lineCap = "round";
							
							ctx.strokeStyle = "rgba("+red+","+green+","+blue+","+opacity+")";
							ctx.lineWidth = width;
							
							this.painting.angle++;

							ctx.arc(startX,startY, distance, angle,this.deg_to_rad(this.painting.angle));


							ctx.stroke();

							if (this.painting.angle == this.rad_to_deg(coords.angle_end)<<0) {
								this.swapAction();	
							};
							
							break;

						case 'line':

							ctx.beginPath();
							ctx.lineCap = "round";
							// this.direction = this.direction * -1;

							ctx.moveTo(coords.x2, coords.y2);

							dist 			= 2 * index;//(a.r()*30)/2 << 0; // bitshift floor magic!
							distance 		= dist + distance;
							
							ctx.lineWidth = width;
							ctx.strokeStyle = "rgba("+red+","+green+","+blue+","+opacity+")";
							
							var na = this.getAngle()*index;
							
							angle 		= angle_end;
							angle_end	= angle + na; 

							this.lastangle = angle_end;
							

							coords = {
								x : startX + distance * Math.cos(angle) << 0,
           						y : startY + distance * Math.sin(angle) << 0,
           						x2: startX  + distance * Math.cos(angle_end) << 0,
           						y2: startY  + distance * Math.sin(angle_end) << 0,
           						angle: angle,
           						angle_end: angle_end
           						
							};

							this.painting.angle = this.rad_to_deg(angle)<<0;
							

							ctx.lineTo(coords.x, coords.y);

							ctx.stroke();

							this.swapAction();
							break;

					}	

				}

				this.rad_to_deg = function(angle){
					return angle * 180 / Math.PI;
				}
				
				this.deg_to_rad = function(angle){
					return angle * Math.PI/180;
				}

				this.getAngle = function(){
					
					// return 0.5 * this.direction;
					return this.direction * (this.deg_to_rad(a.roundNumber( a.r()*360 / a.paths.length, 2 ))) ;
				}
			}


			
			
			app.init();
}