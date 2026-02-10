# الخطوة 4: تشغيل الأداة فعليًا بالمتغيرات + التحقق الآلي للمخرجات

## 1) إجابة سؤالك المختصرة
نعم، أستطيع جعل الأداة تقوم بهذا بشكل مباشر عبر قالب تشغيل جاهز يتضمن:
- ضبط المتغيرات (UI_TECH / ARCH_STYLE / TARGET_MIN_SDK / LEVEL_COUNT_INITIAL)
- إجبار الأداة على الالتزام بـ DELIVERABLE FORMAT
- مراجعة SELF-EVALUATION قبل اعتماد الناتج

---

## 2) قالب تشغيل سريع (انسخه كما هو وعدّل المتغيرات فقط)

```text
[RUN CONFIG]
UI_TECH = Compose
ARCH_STYLE = MVVM
TARGET_MIN_SDK = 24
LEVEL_COUNT_INITIAL = 20

[INSTRUCTION]
Use the provided master prompt for the Android Paint Puzzle game.
Apply the RUN CONFIG values exactly.
Return output strictly in the required DELIVERABLE FORMAT.
Do not skip any section.

[STRICT OUTPUT ENFORCEMENT]
Your output must contain exactly these sections in order:
1) Executive Summary
2) Proposed Architecture
3) Project Structure
4) Kotlin Data Models
5) Core Engine Logic
6) UI Specs for each screen
7) State Management + Persistence Strategy
8) Economy & Shop Rules
9) Edge Cases + Handling
10) Testing Checklist
11) 7-Day Implementation Plan

[SELF-EVALUATION GATE]
Before final answer, provide a Yes/No table for:
- Full screen coverage
- Collision logic completeness
- Progress formula correctness
- Gems/shop consistency
- Edge-case coverage
If any answer is "No", generate Patch v2 and fix only missing parts.
```

---

## 3) نسخة عربية جاهزة للأداة (تنفيذ مباشر)

```text
[إعدادات التشغيل]
UI_TECH = Compose
ARCH_STYLE = MVVM
TARGET_MIN_SDK = 24
LEVEL_COUNT_INITIAL = 20

[المهمة]
استخدم Master Prompt الخاص بلعبة Paint Puzzle على Android.
طبّق إعدادات التشغيل كما هي.
أخرج النتيجة ملتزمة 100% بـ "DELIVERABLE FORMAT" المحدد.
ممنوع حذف أي قسم أو تبديل ترتيب الأقسام.

[تنسيق إلزامي]
1) Executive Summary
2) Proposed Architecture
3) Project Structure
4) Kotlin Data Models
5) Core Engine Logic
6) UI Specs for each screen
7) State Management + Persistence Strategy
8) Economy & Shop Rules
9) Edge Cases + Handling
10) Testing Checklist
11) 7-Day Implementation Plan

[بوابة التقييم الذاتي]
قبل التسليم النهائي أضف جدول (نعم/لا) يشمل:
- تغطية كل الشاشات
- اكتمال منطق التصادم
- صحة معادلة التقدم
- اتساق نظام الجيمز/المتجر
- تغطية الحالات الحدّية
إذا ظهر أي بند = لا، أنشئ Patch v2 يصحّح النقص فقط.
```

---

## 4) متغيرات جاهزة (Profiles)

### Profile A (موصى به لبداية سريعة)
- UI_TECH = Compose
- ARCH_STYLE = MVVM
- TARGET_MIN_SDK = 24
- LEVEL_COUNT_INITIAL = 20

### Profile B (مشروع تقليدي XML)
- UI_TECH = XML
- ARCH_STYLE = MVVM
- TARGET_MIN_SDK = 23
- LEVEL_COUNT_INITIAL = 15

### Profile C (حالة متقدمة)
- UI_TECH = Compose
- ARCH_STYLE = MVI
- TARGET_MIN_SDK = 26
- LEVEL_COUNT_INITIAL = 30

---

## 5) Checklist اعتماد النتيجة (بعد إخراج الأداة)
- [ ] الأقسام الـ 11 موجودة وبالترتيب.
- [ ] منطق الانزلاق حتى العائق مذكور بوضوح.
- [ ] معادلة progress موجودة وصحيحة.
- [ ] الشاشات الأربع كلها مفصّلة.
- [ ] الاقتصاد (Gems/Shop) مكتمل ومتسق.
- [ ] جدول SELF-EVALUATION مرفق.
- [ ] إن وجد نقص: تم إصدار Patch v2.

---

## 6) معيار قبول الخطوة 4
- صار لدينا "Prompt تشغيل" جاهز للأداة مع متغيرات قابلة للتبديل.
- صار لدينا بوابة تحقق SELF-EVALUATION قبل اعتماد الإخراج.
- صار اعتماد الجودة موضوعيًا عبر Checklist واضح.
