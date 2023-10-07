//package com.eddie;
//
//import jdk.incubator.vector.FloatVector;
//import jdk.incubator.vector.VectorSpecies;
//
///**
// * JDK16引入了JEP 338: Vector API (Incubator)提供了jdk.incubator.vector来用于矢量计算
// * JDK17进行改进并作为第二轮的incubatorJEP 414: Vector API (Second Incubator)
// * JDK18的JEP 417: Vector API (Third Incubator)进行改进并作为第三轮的incubator
// * JDK19的JEP 426:Vector API (Fourth Incubator)作为第四轮的incubator
// * JDK20的JEP 438: Vector API (Fifth Incubator)作为第五轮的incubator
// * 而JDK21则作为第六轮的incubator，使用示例如下
// * @author eddie.lys
// * @since 2023/10/7
// */
//public class JEP448VectorAPI {
//    static final VectorSpecies<Float> SPECIES = FloatVector.SPECIES_PREFERRED;
//
//    void vectorComputationA(float[] a, float[] b, float[] c) {
//        int i = 0;
//        int upperBound = SPECIES.loopBound(a.length);
//        for (; i < upperBound; i += SPECIES.length()) {
//            // FloatVector va, vb, vc;
//            var va = FloatVector.fromArray(SPECIES, a, i);
//            var vb = FloatVector.fromArray(SPECIES, b, i);
//            var vc = va.mul(va)
//                    .add(vb.mul(vb))
//                    .neg();
//            vc.intoArray(c, i);
//        }
//        for (; i < a.length; i++) {
//            c[i] = (a[i] * a[i] + b[i] * b[i]) * -1.0f;
//        }
//    }
//    void vectorComputationB(float[] a, float[] b, float[] c) {
//        for (int i = 0; i < a.length; i += SPECIES.length()) {
//            // VectorMask<Float>  m;
//            var m = SPECIES.indexInRange(i, a.length);
//            // FloatVector va, vb, vc;
//            var va = FloatVector.fromArray(SPECIES, a, i, m);
//            var vb = FloatVector.fromArray(SPECIES, b, i, m);
//            var vc = va.mul(va)
//                    .add(vb.mul(vb))
//                    .neg();
//            vc.intoArray(c, i, m);
//        }
//    }
//}
//
