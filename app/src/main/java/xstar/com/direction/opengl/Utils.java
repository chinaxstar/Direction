package xstar.com.direction.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * @author xstar
 * @since 6/29/17.
 */

public class Utils {
    /**
     *
     * @param vertexs 顶点数组
     * @return 浮点型缓冲数据
     */
    public static FloatBuffer getFloatBuffer(float[] vertexs){
        FloatBuffer buffer;
        ByteBuffer qbb = ByteBuffer.allocateDirect(vertexs.length * 4);
        qbb.order(ByteOrder.nativeOrder());
        buffer = qbb.asFloatBuffer();
        //写入数组
        buffer.put(vertexs);
        //设置默认的读取位置
        buffer.position(0);
        return buffer;
    }

    public static ShortBuffer getShortBuffer(short[] shorts){
        ShortBuffer shortBuffer;
        ByteBuffer bb=ByteBuffer.allocateDirect(shorts.length*2);
        bb.order(ByteOrder.nativeOrder());
        shortBuffer=bb.asShortBuffer();
        shortBuffer.put(shorts);
        shortBuffer.position(0);
        return shortBuffer;
    }
}
