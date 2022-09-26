1. 直接看代码，先把 main 方法看明白之后，基本上就会用了。
2. 下面几个方法是 b样曲线 的原理，原理看几个帖子就懂了，我会在下面贴出来。
3. 把 java 代码转成 swift（无脑转，别写错了），然后调试一下，代码里的 genBSplinePoint 就是你要绘制的 b样曲线 数据
4. 代码我已经做了基本的优化，计算量上应该没有问题，你那边需要找到拐点，根据基本的角度判断就能做到

- https://zhuanlan.zhihu.com/p/144042470
- https://www.bilibili.com/video/BV1Dt411f7Qj?p=21&vd_source=6c3e17d95b6cb9cf1a34cbd70425e078
- https://www.bilibili.com/video/BV1Dt411f7Qj?p=22&vd_source=6c3e17d95b6cb9cf1a34cbd70425e078
- https://superjerryshen.github.io/b-spline-demos/#/open-uniform-b-spline