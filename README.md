# ServletMavenDemo
简单的原生Servlet、filter原理探索


   java 依赖、关联、聚合、组合关系   ：  https://blog.csdn.net/qq_31655965/article/details/54645220   
                                      https://blog.csdn.net/zhuyu714997369/article/details/51983871
                                      
                                      https://www.cnblogs.com/wanghuaijun/p/5421419.html
   
   
   创建型模式（5种）：工厂方法模式，抽象工厂模式，单例模式，建造者模式，原型模式。
   结构型模式（7种）：适配器模式，装饰器模式，代理模式，外观模式，桥接模式，组合模式，享元模式。
   行为型模式（11种）：策略模式、模板方法模式、观察者模式、迭代子模式、责任链模式、命令模式、备忘录模式、状态模式、访问者模式、中介者模式、解释器模式。


   Regexp  sql 正则

     explain https://blog.csdn.net/dennis211/article/details/78170079
     
     索引   https://blog.csdn.net/kaka1121/article/details/53395628,    https://blog.csdn.net/samjustin1/article/details/52314813


                 看下利用synchronized实现同步的基础：Java中的每一个对象都可以作为锁。具体表现为以下3种形式。
                     ·对于普通同步方法，锁是当前实例对象。
                     ·对于静态同步方法，锁是当前类的Class对象。
                     ·对于同步方法块，锁是Synchonized括号里配置的对象。


         对象头   https://blog.csdn.net/zhoufanyang_china/article/details/54601311
         
         
          CAS的全称是Compare And Swap 即比较交换，其算法核心思想如下（https://blog.csdn.net/mmoren/article/details/79185862）
                执行函数：CAS(V,E,N)
            其包含3个参数
                V表示要更新的变量
                E表示预期值
                N表示新值
                
                
                此处说的轻量级锁、重量级锁都不是java语言上的锁，而是jvm为了提高锁的获取与释放的效率而做的优化的手段。
Synchronized关键字用的锁是存放在Java对象头的MarkWord里面的，该MarkWord可以不太恰当地认为是一个标记
当使用轻量级锁的时候：线程在执行同步块之前，JVM会在每个线程（即：将会访问同一个同步体的线程）的虚拟机栈中创建用于存储锁记录的空间，并将对象头中的MarkWord复制到虚拟机栈的锁记录中，

在这里面，所谓的对象头，就是指的Synchronized关键字锁住的那个对象的对象头。（注:这个对象就是多线程并发访问的那个共享变量，比如说有一个int类型的对象a，线程1和线程2的任务都是让a自增1，这个a就是存储MarkWord的对象头所属的对象。)
现在继续说轻量级锁的事情。
当将对象头中的MarkWord复制到虚拟机栈中之后，虚拟机将尝试使用CAS将对象头中MarkWord的记录替换为指向该线程的虚拟机栈中的锁记录的指针，如果当前没有线程占有锁或者没有线程竞争锁，则当前线程顺利获取到锁，执行同步体之内的代码。
现在来假设一个场景：当获取到锁的线程执行同步体之内的代码的时候，另一个线程也完成了上面创建锁记录空间，将对象头中的MarkWord复制到自己的锁记录中，尝试用CAS将对象头中的MarkWord修改为指向自己的锁记录的指针，但是由于之前获取到锁的线程已经将MarkWord中的记录修改过了（并且现在还在执行同步体中的代码），与这个现在试图将MarkWord替换为自己的锁记录的线程自己的锁记录中的MarkWord的值不符,CAS操作失败，因此这个线程就会不停地循环使用CAS操作试图将MarkWord替换为自己的记录。这个循环是有次数限制的，如果在循环结束之前CAS操作成功，那么该线程就可以成功获取到锁，如果循环结束之后依然获取不到锁，则锁获取失败，MarkWord中的记录会被修改为指向重量级锁的指针，然后这个获取锁失败的线程就会被挂起，阻塞了。
当持有锁的那个线程执行完同步体之后想用CAS操作将MarkWord中的记录改回它自己的栈中最开始复制的记录的时候会发现MarkWord已被修改为指向重量级锁的指针，因此CAS操作失败，该线程会释放锁并唤起阻塞等待的线程，开始新一轮夺锁之争，而此时，轻量级锁已经膨胀为重量级锁，所有竞争失败的线程都会阻塞，而不是自旋。
轻量级锁一旦膨胀为重量级锁，则不可逆转。因为轻量级锁状态下，自旋是会消耗cpu的，但是锁一旦膨胀，说明竞争激烈，大量线程都做无谓的自旋对cpu是一个极大的浪费
关于自旋锁和轻量级锁的关系，我也很疑惑。在我的理解看来，轻量级锁的自旋CAS就是自旋锁的概念体现，因为线程并没有被挂起，而是做一个忙循环，没有放弃cpu的执行时间片，因此我认为轻量级锁就是自旋锁，但是似乎书籍上并未明确这么说，此处存疑。

---------------------
作者：KogRow 
来源：CSDN 
原文：https://blog.csdn.net/shuaicenglou3032/article/details/77170068?utm_source=copy 
版权声明：本文为博主原创文章，转载请附上博文链接！


CPU多级缓存和缓存一致性        https://blog.csdn.net/Andy2019/article/details/79787888


【JVM】类加载、连接和初始化过程            https://blog.csdn.net/wangyy130/article/details/52105217

深入理解单例模式：静态内部类单例原理        https://blog.csdn.net/mnb65482/article/details/80458571


JDK1.8 JVM运行时数据区域划分             https://blog.csdn.net/bruce128/article/details/79357870


Java里的堆(heap)栈(stack)和方法区(method)      https://www.cnblogs.com/kkcheng/archive/2011/02/25/1964521.html

静态内部类与普通内部类的区别                     https://www.aliyun.com/jiaocheng/298823.html    /  
                                             https://blog.csdn.net/qq_38366777/article/details/78088386




