

public class Camera {
	float x;
	float y;
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Camera(float x,float y){
		this.x = x;
		this.y = y;
	}
	
	public void tick(Entity e,int level){
		float tempX = e.getX()-960; 
		tempX*=-1;
		x += (tempX - x) * 0.02;
		float tempY = e.getY()-540; 
		tempY*=-1;
		y += (tempY - y) * 0.02;
		
		//camera settings for the level
		switch(level){
		case 0:
			if(x > 500) x = 500;
			if(x < 0) x = 0;
			y = 0;
			break;
		case 1:
			if(x > 0) x = 0;
			if(x < -400) x = -400;
			y = 0;
			break;
		case 2:
			x = 0;
			y = 0;
			break;
		case 3:
			if(x > 0) x = 0;
			if(x < -580) x = -580;
			if(y > 700) y = 700;
			if(y < -745) y = -745;
			break;
		case 4:
			if(x > 0) x = 0;
			if(x < -400) x = -400;
			if(y < 0) y = 0;
			if(y > 200) y = 200;
			break;
		case 5:
			x = 0;
			y = 0;
			break;
		case 6:
			y = 0;
			if(x > 0) x = 0;
			if(x < -1050) x = -1050;
		}
	}
}
