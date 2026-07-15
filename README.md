<div align="center">
  <h3>انتخاب زبان / Choose Language</h3>
</div>

<details open>
<summary dir="rtl" align="right"><b>🇮🇷 فارسی (نسخه فارسی را ببندید/باز کنید)</b></summary>
<br>

<div dir="rtl" align="right">

# Silicon Valley: The Tech Cartel 🏙️

پروژه پایانی درس برنامه‌سازی پیشرفته - یک بازی استراتژیک تخته‌ای (Board Game) مشابه بازی کاتان که بر اساس مفاهیم اقتصاد دیجیتال و دنیای تکنولوژی طراحی شده است. در این بازی کارآفرینان برای جمع‌آوری منابع، ساخت شرکت‌ها و تبدیل شدن به اولین Unicorn (یونیکورن) در یک پارک فناوری با یکدیگر رقابت می‌کنند.

## 🛠 تکنولوژی‌های استفاده شده

- **زبان برنامه‌نویسی:** Java (17+)
- **رابط کاربری:** JavaFX

## 🏗 معماری پروژه (Architecture)

این پروژه بر اساس الگوی طراحی MVC (Model-View-Controller) توسعه یافته تا منطق بازی کاملاً از رابط کاربری جدا باشد و کدها در پکیج‌های منطقی زیر دسته‌بندی شده‌اند:

- `logic.engine`: هسته اصلی بازی شامل `GameEngine` (مدیریت نوبت‌ها، تاس، شرایط برد)، `Map` و `AIBrain`.
- `logic.models`: هسته داده‌ها. کلاس انتزاعی (Abstract) `CompanyStructure` که زیرکلاس‌های `MVP`، `Unicorn` و `Partnership` از آن ارث‌بری می‌کنند. همچنین شامل مدل‌های داده‌ای نقشه (`Sector`, `Vertex`, `Edge`) و بازیکنان دارای نقش‌های تخصصی (`AIHackerCEOPlayer`, `AITechGuruPlayer`, `AIVCFundedPlayer` و غیره).
- `ui.controller`: کنترلرهای رابط کاربری JavaFX.
- `ui.view`: فایل‌های FXML مرتبط با رابط کاربری JavaFX.
- `exception`: استثناهای سفارشی مدیریت خطا.
- `logic.save` & `logic.sound`: سیستم مدیریت ذخیره‌سازی و پخش صدا.

## ⚙️ منطق کلی بازی

<ul dir="rtl">
  <li><b>تعداد بازیکنان:</b> ۲ تا ۴ نفر</li>
  <li><b>نقشه بازی:</b> یک شبکه 5x5 شامل ۲۵ سکتور فناوری با منابع اختصاصی. هر سکتور دارای یک عدد فعال‌سازی بین ۲ تا ۱۲ است.</li>
  <li><b>منابع موجود:</b> سرمایه (Capital)، استعداد (Talent)، فضای ابری (Cloud)، پتنت (Patent)، دیتا (Data).</li>
</ul>

## 🏢 سازه‌ها (Structures) و قوانین ساخت

<ul dir="rtl">
  <li><b>قانون فاصله (Distance Rule):</b> هر شرکت ساخته شده باید حداقل ۲ یال (Edge) از شرکت دیگر فاصله داشته باشد.</li>
  <li><b>MVP (محصول اولیه):</b> ساخته شده روی گره‌ها (Vertices). ارزش: ۱ امتیاز. تولید: ۱ منبع. منابع مورد نیاز: ۱ سرمایه، ۱ استعداد، ۱ زیرساخت ابری، ۱ دیتا.</li>
  <li><b>Unicorn (شرکت تک‌شاخ):</b> ارتقای MVP. ارزش: ۲ امتیاز. تولید: ۲ منبع. منابع مورد نیاز: ۳ دیتا، ۲ زیر ساخت ابری.</li>
  <li><b>Partnership (قرارداد همکاری):</b> ساخته شده روی یال‌ها (Edges) برای گسترش شبکه فناوری. منابع مورد نیاز: ۱ سرمایه، ۱ پتنت.</li>
</ul>

## 🎭 راهنمای نقش‌ها (Role Guide)

هر بازیکن می‌تواند نقشی با توانایی‌های منحصر‌به‌فرد انتخاب کند:

| نقش (Role) | مزیت و ویژگی ویژه |
| :--- | :--- |
| **Hacker CEO** | تخفیف در معامله (نرخ تبدیل منابع ۳ به ۱ به جای ۴ به ۱). |
| **Tech Guru** | امکان ارتقای ارزان‌تر سازه‌ها. |
| **VC-Funded** | دریافت سرمایه اولیه بیشتر و مقاومت در برابر بحران (امکان نگهداری تا ۹ کارت به جای ۷ کارت). |

## 🏆 سیستم امتیازدهی و شرط برد

<ul dir="rtl">
  <li><b>ساخت MVP:</b> ۱ امتیاز برای ساخت MVP دریافت می‌کنید.</li>
  <li><b>ارتقا به Unicorn:</b> و اگر به Unicorn ارتقا دهید ۱ امتیاز دیگر هم دریافت می‌کنید.</li>
  <li><b>نقش بنیان‌گذار:</b> ۱ امتیاز منفی اولیه اما دارای مزایای خاص.</li>
  <li><b>بلندترین شبکه:</b> داشتن طولانی‌ترین مسیر شبکه ۲ امتیاز اضافه به همراه دارد.</li>
  <li><b>شرط برد:</b> اولین بازیکنی که به <b>۱۰ امتیاز</b> برسد برنده بازی است.</li>
</ul>

## 🎮 نحوه بازی

<ul dir="rtl">
  <li><b>فاز اولیه (Setup Phase):</b> بازی با دو دور رفت و برگشتی آغاز می‌شود. در این فاز هر بازیکن یک MVP و یک Partnership می‌سازد. پس از پایان این دو دور، سکتورها اولین منابع را توزیع می‌کنند؛ به این صورت که هر MVP دقیقاً یک منبع از سکتورهای مجاور خود دریافت می‌کند.</li>
  <li><b>جریان اصلی بازی و پرتاب تاس:</b> در فاز اصلی، هر بازیکن <i>قبل از هر اقدامی</i> باید تاس بیندازد. اگر مجموع دو عدد تاس با عدد فعال‌سازی (۲ تا ۱۲) سکتوری که بازیکن روی گره‌های آن سازه‌ای دارد برابر باشد، سکتور مربوطه منبع تولید می‌کند (۱ منبع برای MVP و ۲ منبع برای Unicorn). 
    <ul>
      <li><i>نکته:</i> سکتورهایی با نام <b>Regulatory Zone</b> وجود دارند که در هیچ حالتی منبعی تولید نمی‌کنند.</li>
    </ul>
  </li>
  <li><b>بحران قانونی (تاس ۷) و بازرس:</b> اگر عدد تاس ۷ باشد، هیچ منبعی در آن دور توزیع نمی‌شود و بحران قانونی رخ می‌دهد. بازیکنانی که بیش از حد مجاز منبع دارند (بیشتر از ۷ منبع برای حالت عادی و بیشتر از ۹ منبع برای نقش VC-Funded) باید نیمی از منابع خود را به بانک تحویل دهند. سپس بازیکنی که تاس انداخته، باید <b>بازرس (Auditor)</b> را روی یک سکتور قرار دهد. حضور بازرس روی هر سکتور مانع از تولید منابع آن سکتور در صورت آمدن عدد آن می‌شود.</li>
  <li><b>اقدامات پس از تاس:</b> بازیکن می‌تواند پس از توزیع منابع کارهای زیر را انجام دهد:
    <ul>
      <li>ساخت MVP یا ارتقای آن به Unicorn.</li>
      <li>ساخت Partnership.</li>
      <li>خرید از فروشگاه با استفاده از سرمایه (Capital).</li>
      <li>معامله منابع با سایر بازیکنان واقعی (غیر هوش مصنوعی).</li>
      <li>پایان نوبت با کلیک روی کلید <b>END TURN</b>.</li>
    </ul>
  </li>
  <li><b>بازار پویا (Dynamic Market):</b> قیمت اولیه تمام منابع ۴ سرمایه است. قیمت‌ها ثابت نیستند؛ اگر در یک دور منبعی خریداری شود، در دور بعد قیمت آن ۱ واحد افزایش می‌یابد (حداکثر تا سقف ۶). اگر برای ۳ دور متوالی هیچکس آن منبع را نخرد، قیمت آن ۱ واحد کاهش می‌یابد (حداقل تا کف ۲). بازار مسئول مدیریت قیمت‌های پویا و تاریخچه معاملات است.</li>
  <li><b>رابط کاربری و اطلاعات بازیکنان:</b>
    <ul>
      <li>برای ذخیره بازی، کلید <b>Save Game</b> در گوشه سمت راست تعبیه شده است.</li>
      <li>در صفحه اصلی، جدولی وجود دارد که نقش، امتیاز و تعداد کل منابع هر بازیکن را نشان می‌دهد. در نوبت خودتان، با کلیک بر روی عدد «کل منابع»، می‌توانید جزئیات دقیق هر منبعی که در اختیار دارید را مشاهده کنید.</li>
    </ul>
  </li>
</ul>

## 💻 جزئیات فنی

<ul dir="rtl">
  <li><b>مدیریت Threadها:</b> جداسازی کامل منطق بازی (Logic) و عملیات سنگین (مثل ذخیره‌سازی) از Thread اصلی رابط کاربری (UI) جهت جلوگیری از فریز شدن صفحه. آپدیت‌های UI از طریق <code>Platform.runLater()</code> انجام می‌شود.</li>
  <li><b>سیستم ذخیره‌سازی (Save/Load):</b> پیاده‌سازی قابلیت ذخیره و بارگذاری کامل وضعیت بازی (شامل نقشه، بازار و وضعیت بازیکنان) با استفاده از قابلیت Serialization در جاوا.</li>
  <li><b>مدیریت استثناها و پایداری:</b> استفاده از کلاس‌های استثنای سفارشی (مانند <code>InsufficientResourcesException</code> و <code>InvalidPlacementException</code>) برای کنترل دقیق جریان و جلوگیری از کرش کردن بازی.</li>
  <li><b>گزارش تست (No Crash Policy):</b> بازی به گونه‌ای طراحی و تست شده است که تحت هیچ شرایطی (حتی با ورودی‌های نامعتبر بازیکن) کرش نمی‌کند و خطاهای منطقی به درستی هندل می‌شوند.</li>
</ul>

## 🤖 هوش مصنوعی (AI)

بازی دارای ربات‌های هوشمند با کلاس‌های نقش‌محور است که از <code>AIBrain</code> برای تصمیم‌گیری استفاده می‌کنند. هوش مصنوعی قادر به انجام وظایف زیر است:
<ul dir="rtl">
  <li>ساخت MVP و Partnership و ارتقا به Unicorn</li>
  <li>خرید منابع از فروشگاه</li>
  <li>تحویل دادن هوشمندانه منابع هنگام رخ دادن بحران قانونی</li>
  <li>استقرار هدفمند بازرس (Auditor)</li>
</ul>

## 📐 الگوهای طراحی (Design Patterns)

<ul dir="rtl">
  <li><b>الگوی MVC:</b> معماری اصلی پروژه بر پایه تفکیک کامل منطق از رابط کاربری است.</li>
  <li><b>ارث‌بری و چندریختی:</b> استفاده از کلاس انتزاعی <code>CompanyStructure</code> برای تمامی سازه‌ها.</li>
  <li><b>الگوی متد الگو (Template Method):</b> به کار رفته در کلاس <code>CompanyStructure</code> برای تعریف ساختار کلی متدهای مربوط به تولید منبع و امتیازدهی در سازه‌های مختلف.</li>
  <li><b>الگوی استراتژی (Strategy Pattern):</b> این الگو در بخش هوش مصنوعی (AI) برای انتخاب استراتژی‌های مختلف ساخت و معامله به کار رفته است.</li>
  <li><b>الگوی تک‌نسخه (Singleton/Utility Pattern):</b> برای مدیریت و دسترسی متمرکز به منابع مشترک در کلاس‌هایی مانند <code>SoundManager</code> (مدیریت صدا) و <code>SaveManager</code> (مدیریت ذخیره‌سازی).</li>
</ul>


## 🚀 نحوه اجرا

<ol dir="rtl">
  <li><b>نیازمندی‌ها:</b> نصب بودن Java 17 (یا بالاتر) و JavaFX.</li>
  <li>پروژه را در IDE مورد نظر خود (IntelliJ IDEA یا VS Code) باز کنید.</li>
  <li>کلاس اصلی برنامه (Main) را اجرا کنید.</li>
  <li>در صفحه ورود، نام بازیکنان و نقش‌های آن‌ها را انتخاب کرده و وارد بازی شوید.</li>
</ol>

</div>
</details>

---

<details>
<summary><b>🇬🇧 English (Click to expand English version)</b></summary>
<br>

# Silicon Valley: The Tech Cartel 🏙️

Advanced Programming Final Project - A strategic board game inspired by Catan, based on the concepts of the digital economy and the tech world. Players compete as entrepreneurs in a technology park to gather resources, build startups, and become the first to build a Unicorn.

## 🛠 Technologies Used

- **Programming Language:** Java (17+)
- **User Interface:** JavaFX

## 🏗 Project Architecture

This project is developed based on the **MVC (Model-View-Controller)** design pattern to completely decouple game logic from the user interface. The codebase is categorized into the following logical packages:

- `logic.engine`: Core game engine handling turn management, dice, win conditions, and the `AIBrain`.
- `logic.models`: Core data models. Includes the abstract `CompanyStructure` class (parent to `MVP`, `Unicorn`, and `Partnership`), map elements (`Sector`, `Vertex`, `Edge`), and specialized player role classes (e.g., `HackerCEOPlayer`, `VCFundedPlayer`).
- `ui.controller`: JavaFX UI controllers.
- `ui.view`: FXML layout files.
- `exception`: Custom exceptions for specific error handling.
- `logic.save` & `logic.sound`: Game state save/load manager and sound player systems.

## ⚙️ Overall Game Logic

- **Players:** 2 to 4
- **Game Map:** A 5x5 grid containing 25 technology sectors. Each sector has an activation number between 2 and 12.
- **Available Resources:** Capital, Talent, Cloud, Patent, Data.

### 🏢 Structures & Building Rules

- **Distance Rule:** Every constructed company must be at least 2 edges away from any other company.
- **MVP (Minimum Viable Product):** Built on Vertices. Value: 1 point. Production: 1 resource. Requires: 1 Capital, 1 Talent, 1 Cloud, 1 Data.
- **Unicorn:** Upgrade of MVP. Value: 2 points. Production: 2 resources. Requires: 3 Data, 2 Cloud.
- **Partnership:** Built on Edges to expand the tech network. Requires: 1 Capital, 1 Patent.

### 🎭 Role Guide

Players can select roles with unique abilities:

| Role | Special Perk |
| :--- | :--- |
| **Hacker CEO** | Trade discount (3:1 conversion rate instead of 4:1). |
| **Tech Guru** | Upgrading structures requires fewer resources. |
| **VC-Funded** | Higher starting capital and legal crisis resistance (holds up to 9 cards instead of 7). |

### 🏆 Scoring System and Win Condition

- **Building an MVP:** Grants 1 point.
- **Upgrading to Unicorn:** Grants 1 additional point.
- **Founder Role:** Initial -1 point penalty.
- **Longest Network:** Having the longest continuous network path grants an additional 2 points.
- **Win Condition:** The first player to reach **10 points** wins the game.

## 🎮 How to Play

- **Setup Phase:** The game starts with a two-round snake draft. During this phase, each player places one MVP and one Partnership per round. At the end of the setup phase, starting resources are distributed: each MVP generates exactly one resource from its adjacent sectors.
- **Main Phase & Dice Roll:** At the start of every turn, a player *must* roll the dice before taking any other action. If the sum of the dice matches the activation number (2-12) of a sector where players have built structures, that sector produces resources (1 resource for an MVP, 2 resources for a Unicorn).
  - *Regulatory Zones:* These specific sectors never produce resources under any circumstances.
- **Legal Crisis (Rolling a 7) & The Auditor:** Rolling a 7 halts all resource production for that turn and triggers a Legal Crisis. Players holding too many resources (more than 7 normally, or more than 9 for the VC-Funded role) must discard half of their hand to the bank. The player who rolled the 7 must then move the **Auditor** to a new sector. The sector occupied by the Auditor is blocked and will not produce resources even if its number is rolled.
- **Player Actions:** After rolling the dice, a player can:
  - Build an MVP or upgrade an existing MVP to a Unicorn.
  - Build a Partnership to expand their network.
  - Buy resources from the Market using Capital.
  - Trade resources with other human players (non-AI).
  - Click the **END TURN** button to pass the turn.
- **Dynamic Market:** All resources start at a base price of 4 Capital. The prices are not fixed. If a resource is purchased during a round, its price increases by 1 the next round (up to a maximum of 6). If a resource is not bought for 3 consecutive rounds, its price decreases by 1 (down to a minimum of 2). The `Market` model handles history and price tracking.
- **UI Features:**
  - A **Save Game** button is available in the top right corner.
  - The main scoreboard displays each player's role, victory points, and total resource count. During your turn, clicking on your total resource count will expand a detailed breakdown of the specific resources you own.

## 💻 Technical Details

- **Thread Management:** Full separation of game logic and heavy operations (like saving) from the main UI thread to prevent UI freezing. UI updates are handled safely via `Platform.runLater()`.
- **Save/Load System:** Implemented full save/load functionality of the game state (including map, market, and players) using Java Serialization.
- **Exception Management & Stability:** Utilization of custom exception classes (such as `InsufficientResourcesException` and `InvalidPlacementException`) for precise game flow control and crash prevention.
- **No Crash Policy (Testing):** The game is heavily tested and designed strictly to not crash under any circumstances, properly handling all forms of invalid inputs from players.

## 🤖 Artificial Intelligence (AI)

The game features smart bots via role-specific classes, which rely on `AIBrain` for decision-making. The AI is capable of:

- Building MVPs, Partnerships, and upgrading to Unicorns.
- Purchasing from the market based on internal logic.
- Smart resource discarding during a legal crisis.
- Targeted deployment of the Auditor to disrupt opponents.

## 📐 Design Patterns

- **MVC Pattern:** Complete separation of logic from the user interface.
- **Inheritance & Polymorphism:** Using the abstract `CompanyStructure` class for all physical structures.
- **Template Method Pattern:** Applied in the `CompanyStructure` to define the structural logic of resource production and scoring in its subclasses.
- **Strategy Pattern:** Applied in the AI's logic to choose different building and trading strategies dynamically.
- **Singleton / Utility Pattern:** Implemented for centralized access to shared resources, prominently in `SoundManager` and `SaveManager`.

## 🚀 How to Run

1. **Requirements:** Java 17 (or higher) and JavaFX must be installed.
2. Open the project in your preferred IDE (IntelliJ IDEA or VS Code).
3. Run the `Main` application class.
4. On the login screen, enter player names, select their roles, and start the game.

</details>