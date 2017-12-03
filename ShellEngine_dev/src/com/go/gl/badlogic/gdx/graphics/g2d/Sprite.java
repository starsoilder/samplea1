package com.go.gl.badlogic.gdx.graphics.g2d;

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


import static com.go.gl.badlogic.gdx.graphics.g2d.SpriteBatch.C1;
import static com.go.gl.badlogic.gdx.graphics.g2d.SpriteBatch.C2;
import static com.go.gl.badlogic.gdx.graphics.g2d.SpriteBatch.C3;
import static com.go.gl.badlogic.gdx.graphics.g2d.SpriteBatch.C4;
import static com.go.gl.badlogic.gdx.graphics.g2d.SpriteBatch.U1;
import static com.go.gl.badlogic.gdx.graphics.g2d.SpriteBatch.U2;
import static com.go.gl.badlogic.gdx.graphics.g2d.SpriteBatch.U3;
import static com.go.gl.badlogic.gdx.graphics.g2d.SpriteBatch.U4;
import static com.go.gl.badlogic.gdx.graphics.g2d.SpriteBatch.V1;
import static com.go.gl.badlogic.gdx.graphics.g2d.SpriteBatch.V2;
import static com.go.gl.badlogic.gdx.graphics.g2d.SpriteBatch.V3;
import static com.go.gl.badlogic.gdx.graphics.g2d.SpriteBatch.V4;
import static com.go.gl.badlogic.gdx.graphics.g2d.SpriteBatch.X1;
import static com.go.gl.badlogic.gdx.graphics.g2d.SpriteBatch.X2;
import static com.go.gl.badlogic.gdx.graphics.g2d.SpriteBatch.X3;
import static com.go.gl.badlogic.gdx.graphics.g2d.SpriteBatch.X4;
import static com.go.gl.badlogic.gdx.graphics.g2d.SpriteBatch.Y1;
import static com.go.gl.badlogic.gdx.graphics.g2d.SpriteBatch.Y2;
import static com.go.gl.badlogic.gdx.graphics.g2d.SpriteBatch.Y3;
import static com.go.gl.badlogic.gdx.graphics.g2d.SpriteBatch.Y4;
import android.graphics.RectF;

import com.go.gl.badlogic.gdx.MathUtils;
import com.go.gl.badlogic.gdx.NumberUtils;
import com.go.gl.badlogic.gdx.graphics.Color;
import com.go.gl.graphics.Texture;

/** Holds the geometry, color, and texture information for drawing 2D sprites using {@link SpriteBatch}. A Sprite has a position
 * and a size given as width and height. The position is relative to the origin of the coordinate system specified via
 * {@link SpriteBatch#begin()} and the respective matrices. A Sprite is always rectangular and its position (x, y) are located in
 * the bottom left corner of that rectangle. A Sprite also has an origin around which rotations and scaling are performed (that
 * is, the origin is not modified by rotation and scaling). The origin is given relative to the bottom left corner of the Sprite,
 * its position.
 * 
 * <p>
 * <br>精灵类的坐标系Y轴向下。
 * <br>精灵的尺寸必须设置，见{@link #setSize(float, float)}或{@link #setBounds(float, float, float, float)}。
 * <br>精灵拥有一个原点{@link #setOrigin(float, float)}，默认为左下角，旋转和缩放以其为中心。
 * <br>精灵的原点会绘制到精灵的位置{@link #setPosition(float, float)}上。
 * <br>精灵可以使用{@link #setColor(float, float, float, float)}设置一个颜色，和纹理相乘。
 * </p>
 * 
 * @author mzechner
 * @author Nathan Sweet */
//CHECKSTYLE IGNORE 10000 LINES
public class Sprite extends TextureRegion {
	static final int VERTEX_SIZE = 2 + 1 + 2;
	static final int SPRITE_SIZE = 4 * VERTEX_SIZE;

	final float[] vertices = new float[SPRITE_SIZE];
	private final Color color = new Color(1, 1, 1, 1);
	private float x, y;
	float width, height;
	private float originX, originY;
	private float rotation;
	private float scaleX = 1, scaleY = 1;
	private boolean dirty = true;
	private RectF bounds;

	/** Creates an uninitialized sprite. The sprite will need a texture region and bounds set before it can be drawn. */
	public Sprite () {
		setColor(1, 1, 1, 1);
	}

	/** Creates a sprite with width, height, and texture region equal to the size of the texture. */
	public Sprite (Texture texture) {
		this(texture, 0, 0, texture.getWidth(), texture.getHeight());
	}

	/** Creates a sprite with width, height, and texture region equal to the specified size. The texture region's upper left corner
	 * will be 0,0. * @param srcWidth The width of the texture region. May be negative to flip the sprite when drawn.
	 * @param srcHeight The height of the texture region. May be negative to flip the sprite when drawn. */
	public Sprite (Texture texture, int srcWidth, int srcHeight) {
		this(texture, 0, 0, srcWidth, srcHeight);
	}

	/** Creates a sprite with width, height, and texture region equal to the specified size.
	 * @param srcWidth The width of the texture region. May be negative to flip the sprite when drawn.
	 * @param srcHeight The height of the texture region. May be negative to flip the sprite when drawn. */
	public Sprite (Texture texture, int srcX, int srcY, int srcWidth, int srcHeight) {
		if (texture == null) throw new IllegalArgumentException("texture cannot be null.");
		this.texture = texture;
		setRegion(srcX, srcY, srcWidth, srcHeight);
		setColor(1, 1, 1, 1);
		setSize(Math.abs(srcWidth), Math.abs(srcHeight));
		setOrigin(width / 2, height / 2);
	}

	// Note the region is copied.
	public Sprite (TextureRegion region) {
		setRegion(region);
		setColor(1, 1, 1, 1);
		setSize(region.getRegionWidth(), region.getRegionHeight());
		setOrigin(width / 2, height / 2);
	}

	/** Creates a sprite with width, height, and texture region equal to the specified size, relative to specified sprite's texture
	 * region.
	 * @param srcWidth The width of the texture region. May be negative to flip the sprite when drawn.
	 * @param srcHeight The height of the texture region. May be negative to flip the sprite when drawn. */
	public Sprite (TextureRegion region, int srcX, int srcY, int srcWidth, int srcHeight) {
		setRegion(region, srcX, srcY, srcWidth, srcHeight);
		setColor(1, 1, 1, 1);
		setSize(Math.abs(srcWidth), Math.abs(srcHeight));
		setOrigin(width / 2, height / 2);
	}

	/** Creates a sprite that is a copy in every way of the specified sprite. */
	public Sprite (Sprite sprite) {
		set(sprite);
	}

	public void set (Sprite sprite) {
		if (sprite == null) throw new IllegalArgumentException("sprite cannot be null.");
		System.arraycopy(sprite.vertices, 0, vertices, 0, SPRITE_SIZE);
		texture = sprite.texture;
		u = sprite.u;
		v = sprite.v;
		u2 = sprite.u2;
		v2 = sprite.v2;
		x = sprite.x;
		y = sprite.y;
		width = sprite.width;
		height = sprite.height;
		originX = sprite.originX;
		originY = sprite.originY;
		rotation = sprite.rotation;
		scaleX = sprite.scaleX;
		scaleY = sprite.scaleY;
		color.set(sprite.color);
		dirty = sprite.dirty;
	}

	/** Sets the position and size of the sprite when drawn, before scaling and rotation are applied. If origin, rotation, or scale
	 * are changed, it is slightly more efficient to set the bounds after those operations. 
	 * <br>(x, y)为当前原点（默认为左下角）的位置，注意y轴向下，否则可能会生成背面三角形而被剔除导致绘制不出来。
	 * @see {@link #setOrigin(float, float)}
	 * */
	public void setBounds (float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		if (dirty) return;

		float x2 = x + width;
		float y2 = y + height;
		float[] vertices = this.vertices;
		vertices[X1] = x;
		vertices[Y1] = y;

		vertices[X2] = x;
		vertices[Y2] = y2;

		vertices[X3] = x2;
		vertices[Y3] = y2;

		vertices[X4] = x2;
		vertices[Y4] = y;

		if (rotation != 0 || scaleX != 1 || scaleY != 1) dirty = true;
	}

	/** Sets the size of the sprite when drawn, before scaling and rotation are applied. If origin, rotation, or scale are changed,
	 * it is slightly more efficient to set the size after those operations. If both position and size are to be changed, it is
	 * better to use {@link #setBounds(float, float, float, float)}. */
	public void setSize (float width, float height) {
		this.width = width;
		this.height = height;

		if (dirty) return;

		float x2 = x + width;
		float y2 = y + height;
		float[] vertices = this.vertices;
		vertices[X1] = x;
		vertices[Y1] = y;

		vertices[X2] = x;
		vertices[Y2] = y2;

		vertices[X3] = x2;
		vertices[Y3] = y2;

		vertices[X4] = x2;
		vertices[Y4] = y;

		if (rotation != 0 || scaleX != 1 || scaleY != 1) dirty = true;
	}

	/** Sets the position where the sprite will be drawn. If origin, rotation, or scale are changed, it is slightly more efficient
	 * to set the position after those operations. If both position and size are to be changed, it is better to use
	 * {@link #setBounds(float, float, float, float)}. */
	public void setPosition (float x, float y) {
		translate(x - this.x, y - this.y);
	}

	/** Sets the x position where the sprite will be drawn. If origin, rotation, or scale are changed, it is slightly more efficient
	 * to set the position after those operations. If both position and size are to be changed, it is better to use
	 * {@link #setBounds(float, float, float, float)}. */
	public void setX (float x) {
		translateX(x - this.x);
	}

	/** Sets the y position where the sprite will be drawn. If origin, rotation, or scale are changed, it is slightly more efficient
	 * to set the position after those operations. If both position and size are to be changed, it is better to use
	 * {@link #setBounds(float, float, float, float)}. */
	public void setY (float y) {
		translateY(y - this.y);
	}

	/** Sets the x position relative to the current position where the sprite will be drawn. If origin, rotation, or scale are
	 * changed, it is slightly more efficient to translate after those operations. */
	public void translateX (float xAmount) {
		this.x += xAmount;

		if (dirty) return;

		float[] vertices = this.vertices;
		vertices[X1] += xAmount;
		vertices[X2] += xAmount;
		vertices[X3] += xAmount;
		vertices[X4] += xAmount;
	}

	/** Sets the y position relative to the current position where the sprite will be drawn. If origin, rotation, or scale are
	 * changed, it is slightly more efficient to translate after those operations. */
	public void translateY (float yAmount) {
		y += yAmount;

		if (dirty) return;

		float[] vertices = this.vertices;
		vertices[Y1] += yAmount;
		vertices[Y2] += yAmount;
		vertices[Y3] += yAmount;
		vertices[Y4] += yAmount;
	}

	/** Sets the position relative to the current position where the sprite will be drawn. If origin, rotation, or scale are
	 * changed, it is slightly more efficient to translate after those operations. */
	public void translate (float xAmount, float yAmount) {
		x += xAmount;
		y += yAmount;

		if (dirty) return;

		float[] vertices = this.vertices;
		vertices[X1] += xAmount;
		vertices[Y1] += yAmount;

		vertices[X2] += xAmount;
		vertices[Y2] += yAmount;

		vertices[X3] += xAmount;
		vertices[Y3] += yAmount;

		vertices[X4] += xAmount;
		vertices[Y4] += yAmount;
	}

	public void setColor (Color tint) {
		float color = tint.toFloatBits();
		float[] vertices = this.vertices;
		vertices[C1] = color;
		vertices[C2] = color;
		vertices[C3] = color;
		vertices[C4] = color;
	}

	public void setColor (float r, float g, float b, float a) {
		int intBits = ((int)(255 * a) << 24) | ((int)(255 * b) << 16) | ((int)(255 * g) << 8) | ((int)(255 * r));
		float color = NumberUtils.intToFloatColor(intBits);
		float[] vertices = this.vertices;
		vertices[C1] = color;
		vertices[C2] = color;
		vertices[C3] = color;
		vertices[C4] = color;
	}

	public void setColor (float color) {
		float[] vertices = this.vertices;
		vertices[C1] = color;
		vertices[C2] = color;
		vertices[C3] = color;
		vertices[C4] = color;
	}

	/** Sets the origin in relation to the sprite's position for scaling and rotation. */
	public void setOrigin (float originX, float originY) {
		this.originX = originX;
		this.originY = originY;
		dirty = true;
	}

	public void setRotation (float degrees) {
		this.rotation = degrees;
		dirty = true;
	}

	/** Sets the sprite's rotation relative to the current rotation. */
	public void rotate (float degrees) {
		rotation += degrees;
		dirty = true;
	}

	/** Rotates this sprite 90 degrees in-place by rotating the texture coordinates. This rotation is unaffected by
	 * {@link #setRotation(float)} and {@link #rotate(float)}. */
	public void rotate90 (boolean clockwise) {
		float[] vertices = this.vertices;

		if (clockwise) {
			float temp = vertices[V1];
			vertices[V1] = vertices[V4];
			vertices[V4] = vertices[V3];
			vertices[V3] = vertices[V2];
			vertices[V2] = temp;

			temp = vertices[U1];
			vertices[U1] = vertices[U4];
			vertices[U4] = vertices[U3];
			vertices[U3] = vertices[U2];
			vertices[U2] = temp;
		} else {
			float temp = vertices[V1];
			vertices[V1] = vertices[V2];
			vertices[V2] = vertices[V3];
			vertices[V3] = vertices[V4];
			vertices[V4] = temp;

			temp = vertices[U1];
			vertices[U1] = vertices[U2];
			vertices[U2] = vertices[U3];
			vertices[U3] = vertices[U4];
			vertices[U4] = temp;
		}
	}

	public void setScale (float scaleXY) {
		this.scaleX = scaleXY;
		this.scaleY = scaleXY;
		dirty = true;
	}

	public void setScale (float scaleX, float scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		dirty = true;
	}

	/** Sets the sprite's scale relative to the current scale. */
	public void scale (float amount) {
		this.scaleX += amount;
		this.scaleY += amount;
		dirty = true;
	}

	/** Returns the packed vertices, colors, and texture coordinates for this sprite. */
	public float[] getVertices () {
		if (dirty) {
			dirty = false;

			float[] vertices = this.vertices;
			float localX = -originX;
			float localY = -originY;
			float localX2 = localX + width;
			float localY2 = localY + height;
			float worldOriginX = this.x - localX;
			float worldOriginY = this.y - localY;
			if (scaleX != 1 || scaleY != 1) {
				localX *= scaleX;
				localY *= scaleY;
				localX2 *= scaleX;
				localY2 *= scaleY;
			}
			if (rotation != 0) {
				final float cos = MathUtils.cosDeg(rotation);
				final float sin = MathUtils.sinDeg(rotation);
				final float localXCos = localX * cos;
				final float localXSin = localX * sin;
				final float localYCos = localY * cos;
				final float localYSin = localY * sin;
				final float localX2Cos = localX2 * cos;
				final float localX2Sin = localX2 * sin;
				final float localY2Cos = localY2 * cos;
				final float localY2Sin = localY2 * sin;

				final float x1 = localXCos - localYSin + worldOriginX;
				final float y1 = localYCos + localXSin + worldOriginY;
				vertices[X1] = x1;
				vertices[Y1] = y1;

				final float x2 = localXCos - localY2Sin + worldOriginX;
				final float y2 = localY2Cos + localXSin + worldOriginY;
				vertices[X2] = x2;
				vertices[Y2] = y2;

				final float x3 = localX2Cos - localY2Sin + worldOriginX;
				final float y3 = localY2Cos + localX2Sin + worldOriginY;
				vertices[X3] = x3;
				vertices[Y3] = y3;

				vertices[X4] = x1 + (x3 - x2);
				vertices[Y4] = y3 - (y2 - y1);
			} else {
				final float x1 = localX + worldOriginX;
				final float y1 = localY + worldOriginY;
				final float x2 = localX2 + worldOriginX;
				final float y2 = localY2 + worldOriginY;

				vertices[X1] = x1;
				vertices[Y1] = y1;

				vertices[X2] = x1;
				vertices[Y2] = y2;

				vertices[X3] = x2;
				vertices[Y3] = y2;

				vertices[X4] = x2;
				vertices[Y4] = y1;
			}
		}
		return vertices;
	}

	/** Returns the bounding axis aligned {@link Rectangle} that bounds this sprite. The rectangles x and y coordinates describe its
	 * bottom left corner. If you change the position or size of the sprite, you have to fetch the triangle again for it to be
	 * recomputed.
	 * 
	 * @return the bounding Rectangle */
	public RectF getBoundingRectangle () {
		final float[] vertices = getVertices();

		float minx = vertices[X1];
		float miny = vertices[Y1];
		float maxx = vertices[X1];
		float maxy = vertices[Y1];

		minx = minx > vertices[X2] ? vertices[X2] : minx;
		minx = minx > vertices[X3] ? vertices[X3] : minx;
		minx = minx > vertices[X4] ? vertices[X4] : minx;

		maxx = maxx < vertices[X2] ? vertices[X2] : maxx;
		maxx = maxx < vertices[X3] ? vertices[X3] : maxx;
		maxx = maxx < vertices[X4] ? vertices[X4] : maxx;

		miny = miny > vertices[Y2] ? vertices[Y2] : miny;
		miny = miny > vertices[Y3] ? vertices[Y3] : miny;
		miny = miny > vertices[Y4] ? vertices[Y4] : miny;

		maxy = maxy < vertices[Y2] ? vertices[Y2] : maxy;
		maxy = maxy < vertices[Y3] ? vertices[Y3] : maxy;
		maxy = maxy < vertices[Y4] ? vertices[Y4] : maxy;

		if (bounds == null) bounds = new RectF();
		bounds.left = minx;
		bounds.top = miny;
		bounds.right = maxx;
		bounds.bottom = maxy;
		return bounds;
	}

	public void draw (SpriteBatch spriteBatch) {
		spriteBatch.draw(texture, getVertices(), 0, SPRITE_SIZE);
	}

	public void draw (SpriteBatch spriteBatch, float alphaModulation) {
		Color color = getColor();
		float oldAlpha = color.a;
		color.a *= alphaModulation;
		setColor(color);
		draw(spriteBatch);
		color.a = oldAlpha;
		setColor(color);
	}

	public float getX () {
		return x;
	}

	public float getY () {
		return y;
	}

	public float getWidth () {
		return width;
	}

	public float getHeight () {
		return height;
	}

	public float getOriginX () {
		return originX;
	}

	public float getOriginY () {
		return originY;
	}

	public float getRotation () {
		return rotation;
	}

	public float getScaleX () {
		return scaleX;
	}

	public float getScaleY () {
		return scaleY;
	}

	/** Returns the color of this sprite. Changing the returned color will have no affect, {@link #setColor(Color)} or
	 * {@link #setColor(float, float, float, float)} must be used. */
	public Color getColor () {
		float floatBits = vertices[C1];
		int intBits = NumberUtils.floatToIntColor(vertices[C1]);
		Color color = this.color;
		color.r = (intBits & 0xff) / 255f;
		color.g = ((intBits >>> 8) & 0xff) / 255f;
		color.b = ((intBits >>> 16) & 0xff) / 255f;
		color.a = ((intBits >>> 24) & 0xff) / 255f;
		return color;
	}

	public void setRegion (float u, float v, float u2, float v2) {
		super.setRegion(u, v, u2, v2);

		float[] vertices = Sprite.this.vertices;
		vertices[U1] = u;
		vertices[V1] = v2;

		vertices[U2] = u;
		vertices[V2] = v;

		vertices[U3] = u2;
		vertices[V3] = v;

		vertices[U4] = u2;
		vertices[V4] = v2;
	}

	public void setU (float u) {
		super.setU(u);
		vertices[U1] = u;
		vertices[U2] = u;
	}

	public void setV (float v) {
		super.setV(v);
		vertices[V2] = v;
		vertices[V3] = v;
	}

	public void setU2 (float u2) {
		super.setU2(u2);
		vertices[U3] = u2;
		vertices[U4] = u2;
	}

	public void setV2 (float v2) {
		super.setV2(v2);
		vertices[V1] = v2;
		vertices[V4] = v2;
	}

	public void flip (boolean x, boolean y) {
		super.flip(x, y);
		float[] vertices = Sprite.this.vertices;
		if (x) {
			float temp = vertices[U1];
			vertices[U1] = vertices[U3];
			vertices[U3] = temp;
			temp = vertices[U2];
			vertices[U2] = vertices[U4];
			vertices[U4] = temp;
		}
		if (y) {
			float temp = vertices[V1];
			vertices[V1] = vertices[V3];
			vertices[V3] = temp;
			temp = vertices[V2];
			vertices[V2] = vertices[V4];
			vertices[V4] = temp;
		}
	}

	public void scroll (float xAmount, float yAmount) {
		float[] vertices = Sprite.this.vertices;
		if (xAmount != 0) {
			float u = (vertices[U1] + xAmount) % 1;
			float u2 = u + width / texture.getWidth();
			this.u = u;
			this.u2 = u2;
			vertices[U1] = u;
			vertices[U2] = u;
			vertices[U3] = u2;
			vertices[U4] = u2;
		}
		if (yAmount != 0) {
			float v = (vertices[V2] + yAmount) % 1;
			float v2 = v + height / texture.getHeight();
			this.v = v;
			this.v2 = v2;
			vertices[V1] = v2;
			vertices[V2] = v;
			vertices[V3] = v;
			vertices[V4] = v2;
		}
	}
}
