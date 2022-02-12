# Add any ProGuard configurations specific to this
# extension here.

-keep public class xyz.kumaraswamy.ai2tools.Ai2Tools {
    public *;
 }
-keeppackagenames gnu.kawa**, gnu.expr**

-optimizationpasses 4
-allowaccessmodification
-mergeinterfacesaggressively

-repackageclasses 'xyz/kumaraswamy/ai2tools/repack'
-flattenpackagehierarchy
-dontpreverify
