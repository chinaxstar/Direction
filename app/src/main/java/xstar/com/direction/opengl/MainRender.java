package xstar.com.direction.opengl;

import android.opengl.GLSurfaceView;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author xstar
 * @since 6/29/17.
 */

public class MainRender implements GLSurfaceView.Renderer {


    /**
     * 正方体
     * （x,y,z）6面  4×6=24
     * <p>
     * 中心为（0,0,0）
     * 左面 x=-0.5
     * 右面 x=0.5
     * 上面 y=0.5
     * 下面 y=-0.5
     * 前面 z=0.5
     * 后面 z=-0.5
     */
    private float[] mArray = {
            //左面
            -0.5F, -0.5F, 0.5F,
            -0.5F, 0.5F, -0.5F,
            -0.5F, 0.5F, 0.5F,
            -0.5f, -0.5f, -0.5f,
            //右面
            0.5F, -0.5F, 0.5F,
            0.5F, 0.5F, -0.5F,
            0.5F, 0.5F, 0.5F,
            0.5f, -0.5f, -0.5f,
            //上面
            -0.5F, 0.5F, 0.5F,
            0.5F, 0.5F, -0.5F,
            0.5F, 0.5F, 0.5F,
            -0.5f, 0.5f, -0.5f,
            //下面
            0.5F, -0.5F, -0.5F,
            -0.5F, -0.5F, 0.5F,
            0.5F, -0.5F, 0.5F,
            -0.5f, -0.5f, -0.5f,
            //前面
            -0.5F, 0.5F, 0.5F,
            0.5f, -0.5f, 0.5f,
            0.5F, 0.5F, 0.5F,
            -0.5F, -0.5F, 0.5F,

            -0.5F, 0.5F, -0.5F,
            0.5f, -0.5f, -0.5f,
            -0.5F, -0.5F, -0.5F,
            0.5F, 0.5F, -0.5F,
    };//顶点数据

    //顶点颜色数组
    private float[] mcolorArray = {

            1f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f,
            1f, 0f, 0f, 1f,

    };

    private short[] indices={
            0,1,2,
            0,2,3,

            4,5,6,
            4,5,7,

            8,9,10,
            8,9,11,

            12,13,14,
            12,13,15,

            16,17,18,
            16,17,19,

            20,21,22,
            20,21,23,
    };

    private FloatBuffer buffer;
    private FloatBuffer colorBuffer;
    private ShortBuffer indicesBuffer;
    public MainRender() {
        buffer = Utils.getFloatBuffer(mArray);
        colorBuffer = Utils.getFloatBuffer(mcolorArray);
        indicesBuffer=Utils.getShortBuffer(indices);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0, 0, 0, 0);//清屏 黑色

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(width >> 2, width >> 1, width >> 1, height >> 1);//设置串口大小

    }

int rotate=0;
    //  surface绘制时使用
    @Override
    public void onDrawFrame(GL10 gl) {
        //脏的时候渲染
        if (glSurfaceView != null) glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT|GL10.GL_DEPTH_BUFFER_BIT);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//开启顶点数组
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        gl.glLoadIdentity();
        gl.glRotatef(rotate,1,1f,-1f);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, buffer);//每组数据大小 类型 锯齿宽度 数据

        gl.glColorPointer(4, GL10.GL_FIXED, 0, colorBuffer);

//        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 24);
        gl.glDrawElements(GL10.GL_TRIANGLES,indices.length,GL10.GL_UNSIGNED_SHORT,indicesBuffer);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);//禁止顶点数组
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        rotate++;
    }


    private GLSurfaceView glSurfaceView;

    public GLSurfaceView getGlSurfaceView() {
        return glSurfaceView;
    }

    public void setGlSurfaceView(GLSurfaceView glSurfaceView) {
        this.glSurfaceView = glSurfaceView;
    }
}
