# الخطوة 2: تصميم هيكل البرومبت الاحترافي (Prompt Architecture)

## 1) هدف هذه الخطوة
تحويل متطلبات الخطوة 1 إلى **قالب برومبت احترافي قابل لإعادة الاستخدام** لإنتاج لعبة Android (Kotlin) بدقة عالية، مع تقليل الغموض والأخطاء.

## 2) الهيكل القياسي للبرومبت (Prompt Skeleton)

### A) الدور (Persona)
- عرّف النموذج كخبير واضح التخصص:
  - "أنت مهندس ألعاب موبايل خبير في Kotlin + Android Studio + Game Architecture + UX للألعاب الكاجوال."

### B) الهدف (Goal)
- صياغة صريحة لما نريده من المخرجات:
  - "ابنِ مخطط تنفيذ كامل + كود مبدئي قابل للتشغيل للعبة Paint Puzzle بنظام الانزلاق حتى الاصطدام."

### C) السياق (Context)
- وضع وصف اللعبة المختصر + القيود التقنية:
  - منصة Android.
  - لغة Kotlin.
  - نمط رسوم Flat Vector.
  - 4 شاشات أساسية (Main, Gameplay, Win, Shop).

### D) نطاق العمل (Scope)
- تحديد المطلوب بدقة داخل المخرج:
  1. Architecture مقترح.
  2. بنية الملفات.
  3. نماذج البيانات.
  4. منطق الحركة والاصطدام والتقدم.
  5. UI state management.
  6. خطة اختبار.

### E) القيود (Constraints)
- قيود إلزامية لتقليل الإجابات العامة:
  - لا تكتب وصفًا نظريًا فقط؛ أعطِ خطوات تنفيذ عملية.
  - لا تتجاهل أي شاشة من الشاشات الأربع.
  - لا تستخدم حركة خطوة-بخطوة؛ الحركة انزلاقية حتى العائق.
  - التزم بتنسيق إخراج محدد.

### F) تنسيق الإخراج (Output Format)
- هيكل إخراج ثابت مثال:
  1. ملخص تنفيذي.
  2. بنية المشروع.
  3. كود/شبه كود للمحرك الأساسي.
  4. UI Flow لكل شاشة.
  5. حالات حدّية.
  6. خطة تنفيذ 7 أيام.

### G) معايير النجاح (Success Criteria)
- اجعل النموذج يراجع نفسه قبل التسليم:
  - هل غطيت كل المتطلبات الوظيفية؟
  - هل يوجد منطق تصادم صحيح؟
  - هل التقدم يصل 100% بطريقة قابلة للحساب؟

### H) آلية التحسين (Iteration Hook)
- أضف مقطعًا ثابتًا لطلب التحسين:
  - "إذا كان أي جزء ناقص، قدم نسخة v2 مع تصحيح النقص فقط دون إعادة كل شيء."

## 3) القالب العملي (Reusable Master Template)

```text
[ROLE]
أنت مهندس ألعاب موبايل Senior متخصص في Kotlin + Android Studio + Casual Puzzle Design.

[TASK]
أريد منك إنتاج مخطط تنفيذ وكود مبدئي للعبة Paint Puzzle بنمط Swipe-to-Slide (التحرك حتى الاصطدام) على Android.

[CONTEXT]
- اللعبة 2D Front View لواجهة منزل.
- الهدف: طلاء 100% من المساحات البيضاء.
- العوائق: نوافذ، أبواب، فتحات، شجيرات، عوارض.
- الشاشات المطلوبة: Main, Gameplay, Win, Shop.
- الاقتصاد: Gems + شراء Skins.

[REQUIREMENTS]
1) Core mechanics:
   - Swipe 4 اتجاهات.
   - الحركة خطية حتى التصادم.
   - trail paint + progress calculation.
2) UI/UX:
   - Progress bar + Level + Gems.
   - Win with confetti + next level.
   - Shop grid + unlock/purchase/select skin.
3) Architecture:
   - اقترح طبقات واضحة وفصل state عن rendering.
4) Persistence:
   - حفظ المستوى، الجيمز، المشتريات.

[CONSTRAINTS]
- لا إجابات عامة.
- لا حذف لأي شاشة.
- لا تغيير للميكانيكية الأساسية.
- استخدم Kotlin-oriented structure قابل للتطبيق مباشرة.

[OUTPUT FORMAT]
قدّم الإجابة بالأقسام التالية وبنفس الترتيب:
1. Executive Summary (5-8 bullets)
2. Project Structure (folders/files)
3. Data Models (Kotlin data classes)
4. Core Engine Logic (movement/collision/progress)
5. UI Screen Specs (Main/Gameplay/Win/Shop)
6. State Management & Persistence
7. Edge Cases & Failure Handling
8. Testing Checklist
9. 7-Day Implementation Plan

[QUALITY CHECK]
قبل التسليم، راجع:
- تغطية كل المتطلبات.
- دقة حساب progress.
- اتساق الاقتصاد (gems/shop).
- عدم وجود تعارضات منطقية.

[ITERATION]
إذا وجدت نواقص، قدم Patch v2 مختصرًا يشرح فقط ما تم إصلاحه.
```

## 4) المتغيرات القابلة للتخصيص (Prompt Variables)
- `TARGET_MIN_SDK` (مثال: 24)
- `UI_TECH` (Compose أو XML)
- `ARCH_STYLE` (MVI / MVVM)
- `LEVEL_COUNT_INITIAL` (مثال: 20)
- `GEMS_REWARD_RANGE` (مثال: 20-60)
- `VISUAL_THEME` (Pastel / Vibrant)

## 5) Anti-Patterns يجب منعها داخل البرومبت
- طلب "أنشئ لعبة كاملة" بدون هيكل إخراج.
- إهمال تحديد فيزياء الحركة.
- عدم تحديد شكل التسليم (يؤدي لنص إنشائي غير قابل للتنفيذ).
- عدم طلب checklist تقييم ذاتي من النموذج.

## 6) معيار قبول الخطوة 2
- وجود Prompt Skeleton واضح (Persona/Goal/Context/Constraints/Format).
- وجود قالب قابل للنسخ الفوري (Reusable Template).
- وجود متغيرات Customization للسيناريوهات المختلفة.
- وجود ضوابط تمنع المخرجات العامة منخفضة الجودة.

## 7) مخرجات الخطوة التالية
- في الخطوة 3 سنولّد **Master Prompt النهائي** (نسخة عربية + نسخة إنجليزية تقنية) اعتمادًا على هذا الهيكل مباشرة.
