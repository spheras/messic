function startFx(canvas,stopFunction){
			var app = {
				pi: Math.PI,
				r: Math.random,
				m: Math,
				init: function(){
					var self = this;
					this.count = 0;
					this.$obj = $(canvas);
					this.ctx = canvas.getContext('2d');
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
			 		for (var i = 0; i < 5	; i++) {
			 			
			 				var x = self.width/2;
			 				var y = self.height/2;
			 			
			 			self.paths.push(new bPath(x, y));
			 		};
			 		

			 		this.anim = window.setInterval(this.draw,16)
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
					//console.log('draw');
					var self = app;
					
					// self.factor = self.iter();

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

			function bPath(startX, startY) {

				var distance = 0
				var dist 	= 0;
				var a 			= app;

				var direction 	= -1; // 1 or -1
				this.direction = -1;
				var ctx 		= canvas.getContext('2d');
				var that 		= this;

				var opacity		= a.roundNumber(a.r() * 0.5+0.5,1);
				var red 		= a.r() * 256 << 0;
				var green		= a.r() * 16 << 0;
				var blue 		= a.r() * 16 << 0;
				var width 		= 0.5 // a.roundNumber(a.r() * 2,2);

				var compositeTypes = [
				  'source-over','source-in','source-out','source-atop',
				  'destination-over','destination-in','destination-out','destination-atop',
				  'lighter','darker','copy','xor'
				];

				var lastaction 	= 'arc'; // arc or line
				var angle 		= 0;
				var angle_end	= 0;
				var coords = {
					x : null,
					y : null,
					x2 : startX,
					y2 : startY
				};

				// ctx.moveTo(startX, startY);
				// ctx.beginPath();
				console.log(coords);
				var i = 0;

				this.updatePosition = function(){
					
					if (distance > startX ) {
						distance = 0;
						coords.x2 = startX;
						coords.y2 = startY;
						lastaction = 'arc';
						red 		= a.r() * 256 << 0;
						green		= a.r() * 64 << 0;
						blue 		= a.r() * 64 << 0;
					};

					lastaction = (lastaction == 'arc') ? 'line' : 'arc';

					switch (lastaction){

						case 'arc':

							ctx.beginPath();
							ctx.moveTo(coords.x,coords.y);
							
							
							ctx.strokeStyle = "rgba("+red+","+green+","+blue+","+opacity+")";
							ctx.lineWidth = 4;
							
							ctx.arc(startX,startY, distance, angle,angle_end);


							ctx.stroke();

							break;

						case 'line':

							ctx.beginPath();
							
							this.direction = this.direction * -1;

							ctx.moveTo(coords.x2, coords.y2);

							dist 			= (a.r()*150)/2 << 0; // bitshift floor magic!
							distance 		= dist + distance;
							
							
							ctx.lineWidth = width;
							ctx.strokeStyle = "rgba("+red+","+green+","+blue+","+opacity+")";
							

							
							
							angle 		= angle_end;
							angle_end	= angle + this.getAngle(); 
							

							coords = {
								x : startX + distance * Math.cos(angle) << 0,
           						y : startY + distance * Math.sin(angle) << 0,
           						x2: startX  + distance * Math.cos(angle_end) << 0,
           						y2: startY  + distance * Math.sin(angle_end) << 0
           						
							};

							

							ctx.lineTo(coords.x, coords.y);

							ctx.stroke();


							break;

					}	

				}

				this.getAngle = function(){
					
					// return 0.5 * this.direction;
					return this.direction * (a.roundNumber( a.r()*360, 2 ) * Math.PI/180);
				}
			}


			
			
			app.init();
}