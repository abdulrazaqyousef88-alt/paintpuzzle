# الخطوة 3: Master Prompt النهائي (عربي + إنجليزي)

## 1) الهدف من هذه الخطوة
تقديم برومبتين نهائيين جاهزين للنسخ والاستخدام:
1. نسخة عربية واضحة.
2. نسخة إنجليزية تقنية موجهة لأدوات توليد الكود.

كلا النسختين مبنيتان على:
- خطة المشروع.
- متطلبات الخطوة 1.
- هيكل البرومبت في الخطوة 2.

---

## 2) النسخة العربية (جاهزة للنسخ)

```text
[ROLE]
أنت مهندس ألعاب موبايل Senior وخبير Kotlin + Android Studio، ومتخصص في تصميم ألعاب Puzzle كاجوال قابلة للتوسع.

[GOAL]
أريد منك إنتاج مخطط تنفيذ احترافي + كود مبدئي قابل للتشغيل (Playable Prototype) للعبة Paint Puzzle على Android.

[GAME CONCEPT]
اللعبة عبارة عن واجهة منزل 2D، والهدف هو طلاء 100% من المساحات البيضاء.
اللاعب يتحكم بإسفنجة عبر السحب (Swipe) في 4 اتجاهات.
الإسفنجة تنزلق بخط مستقيم حتى تصطدم بعائق (لا يوجد حركة خطوة-بخطوة).

[CORE MECHANICS]
1) Swipe-to-Slide in 4 directions.
2) Collision with house boundaries, windows, doors, vents, bushes, beams.
3) Sponge paints all traversed paintable cells.
4) Progress = paintedPaintable / totalPaintable * 100.
5) Win when progress reaches 100%.

[SCREENS REQUIRED]
1) Main/Splash Screen:
   - Game logo
   - Big Play button
   - Simple animated background
2) Gameplay Screen:
   - Progress bar top
   - House playfield center
   - Level number + Gems
3) Win Screen:
   - Success state + confetti effect
   - Gems reward display
   - Next Level button
4) Shop Screen:
   - Skin grid (sponge/cat/ball...)
   - Purchase/select using gems

[VISUAL STYLE]
- Flat Vector Art
- Vibrant saturated colors
- High contrast between white wall and paint color
- Optional juice effects: paint splashes + light hit shake

[TECHNICAL REQUIREMENTS]
- Platform: Android
- Language: Kotlin
- UI choice: (Compose or XML) -> اختر الأنسب وعلل الاختيار
- Architecture: MVVM أو MVI مع فصل واضح بين game logic وUI
- Persistence: حفظ (current level, gems, owned skins, selected skin)

[DELIVERABLE FORMAT - STRICT]
قدّم النتيجة بالأقسام التالية وبنفس الترتيب:
1) Executive Summary (نقاط قصيرة واضحة)
2) Proposed Architecture (modules/layers/responsibilities)
3) Project Structure (folders + key files)
4) Kotlin Data Models
5) Core Engine Logic (movement/collision/paint/progress/win)
6) UI Specs for each screen (Main/Gameplay/Win/Shop)
7) State Management + Persistence Strategy
8) Economy & Shop Rules
9) Edge Cases + Handling
10) Testing Checklist (unit + integration + UI)
11) 7-Day Implementation Plan

[CONSTRAINTS]
- لا تقدّم وصفًا إنشائيًا عامًا.
- لا تحذف أي شاشة أو ميكانيكية أساسية.
- لا تستخدم step-by-step movement؛ الحركة انزلاقية حتى العائق.
- اجعل المخرجات قابلة للتحويل مباشرة إلى كود Android.

[SELF-EVALUATION]
قبل التسليم، أضف جدول تحقق يجيب بنعم/لا على:
- هل غطيت كل الشاشات؟
- هل منطق التصادم كامل؟
- هل معادلة التقدم صحيحة؟
- هل نظام الجيمز/المتجر متسق؟
- هل يوجد حالات حدّية مغطاة؟

[ITERATION INSTRUCTION]
إذا كان أي جزء ناقص، قدّم Patch v2 مختصرًا يتضمن فقط:
- ما الذي كان ناقصًا
- كيف تم إصلاحه
- أثر الإصلاح على بنية الحل
```

---

## 3) English Technical Version (Ready to copy)

```text
[ROLE]
You are a Senior Mobile Game Engineer specialized in Kotlin, Android Studio, and scalable casual puzzle architecture.

[OBJECTIVE]
Generate an implementation-ready plan plus starter code blueprint for an Android Paint Puzzle game (playable prototype quality).

[GAME OVERVIEW]
The game is a 2D front-view house painting puzzle.
Goal: paint 100% of white paintable areas.
Control: 4-direction swipe.
Movement rule: the sponge slides linearly until collision (NOT tile-by-tile stepping).

[CORE GAMEPLAY RULES]
1. Swipe Up/Down/Left/Right to move.
2. Sponge continues moving until it hits an obstacle.
3. Obstacles include: house borders, windows, doors, vents, bushes, wooden beams.
4. All traversed paintable cells become painted.
5. Progress formula: paintedPaintable / totalPaintable * 100.
6. Win condition: progress reaches 100%.

[REQUIRED SCREENS]
1. Main/Splash: logo, large Play button, simple animated background.
2. Gameplay: top progress bar, central house playfield, level index, gems counter.
3. Win: completion state, confetti, reward display, next-level button.
4. Shop: skin grid, purchase/unlock/select using gems.

[VISUAL DIRECTION]
- Flat vector art style.
- Vibrant saturated palette.
- High contrast between white walls and paint trail.
- Optional juice effects: paint splash on collision, subtle hit shake.

[TECH STACK CONSTRAINTS]
- Platform: Android
- Language: Kotlin
- UI: choose Compose or XML and justify briefly.
- Architecture: MVVM or MVI with strict separation between game engine state and rendering.
- Persistence required for: current level, gems, owned skins, selected skin.

[OUTPUT FORMAT - STRICT ORDER]
1. Executive Summary (5–8 bullets)
2. Architecture Proposal (layers/modules and responsibilities)
3. Project Structure (folders/files)
4. Kotlin Data Models
5. Core Engine Logic (movement/collision/paint/progress/win)
6. Screen-by-Screen UI Specs
7. State Management + Persistence Plan
8. Economy and Shop Rules
9. Edge Cases and Failure Handling
10. Testing Checklist (unit/integration/UI)
11. 7-Day Delivery Plan

[HARD CONSTRAINTS]
- No generic explanations.
- Do not omit any required screen or core mechanic.
- Do not switch to step-by-step movement.
- Keep output implementation-oriented and directly actionable.

[SELF-CHECK]
Before final output, include a Yes/No validation table for:
- Full screen coverage
- Collision logic completeness
- Progress formula correctness
- Gems/shop consistency
- Edge-case coverage

[ITERATION MODE]
If anything is missing, provide a concise Patch v2 that includes only:
- Missing item(s)
- Exact fix
- Architectural impact
```

---

## 4) استخدام مباشر (How to use quickly)
1) اختر النسخة العربية أو الإنجليزية حسب الأداة.
2) أضف متغيراتك:
   - UI_TECH = Compose أو XML
   - ARCH_STYLE = MVVM أو MVI
   - TARGET_MIN_SDK
   - LEVEL_COUNT_INITIAL
3) اطلب من الأداة إخراجًا مطابقًا لـ "DELIVERABLE FORMAT".
4) راجع جدول SELF-EVALUATION قبل اعتماد الناتج.

## 5) معيار قبول الخطوة 3
- وجود نسختين نهائيتين (AR + EN) قابلتين للنسخ المباشر.
- تضمين صريح لكل المتطلبات الأساسية للعبة.
- تضمين قيود صارمة تمنع الردود الإنشائية.
- تضمين آلية تقييم ذاتي + Iteration Patch.
