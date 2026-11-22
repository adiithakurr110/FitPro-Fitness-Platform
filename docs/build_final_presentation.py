import os
import sys
import copy
from pptx import Presentation
from pptx.util import Inches, Pt
from pptx.dml.color import RGBColor
from pptx.enum.shapes import MSO_SHAPE
from pptx.enum.text import PP_ALIGN

SRC_PPTX = r'D:\fitpro\fitpro\docs\FitPro_Presentation_v3.pptx'
OUT_PPTX = r'D:\fitpro\fitpro\docs\FitPro_Final_Presentation.pptx'
SC_DIR = r'D:\fitpro\fitpro\docs\screenshots'

# Color Palette
CLR_BG_DARK = RGBColor(20, 33, 61)       # #14213D - Deep Navy
CLR_CARD_BG = RGBColor(31, 46, 82)       # #1F2E52 - Card Navy
CLR_WHITE = RGBColor(255, 255, 255)      # Pure White
CLR_MUTED = RGBColor(148, 163, 184)      # Slate Gray #94A3B8
CLR_ACCENT_BLUE = RGBColor(0, 113, 227)  # Apple Blue #0071E3
CLR_ACCENT_ORANGE = RGBColor(255, 90, 31)# Warm Orange #FF5A1F
CLR_ACCENT_GREEN = RGBColor(22, 163, 74) # Emerald Green #16A34A
CLR_BORDER = RGBColor(40, 64, 122)       # Card Border

def set_text(shape, text, font_size=None, font_color=None, bold=None, align=None):
    if not shape.has_text_frame:
        return
    tf = shape.text_frame
    tf.clear()
    p = tf.paragraphs[0]
    p.text = text
    if align:
        p.alignment = align
    if font_size or font_color or bold is not None:
        if font_size:
            p.font.size = font_size
        if font_color:
            p.font.color.rgb = font_color
        if bold is not None:
            p.font.bold = bold

def replace_image(slide, shape_name, new_image_path):
    pic_shape = None
    for s in slide.shapes:
        if s.name == shape_name and s.shape_type == 13:
            pic_shape = s
            break
    if not pic_shape:
        print(f"Warning: Picture shape {shape_name} not found on slide")
        return
    blip = pic_shape._element.xpath('.//a:blip')[0]
    rId = blip.get('{http://schemas.openxmlformats.org/officeDocument/2006/relationships}embed')
    part = slide.part.related_part(rId)
    with open(new_image_path, 'rb') as f:
        part._blob = f.read()
    print(f"Replaced image for {shape_name} with {os.path.basename(new_image_path)}")

def format_card(card_shape, bg_color=CLR_CARD_BG, border_color=CLR_BORDER, border_width=Pt(1)):
    card_shape.fill.solid()
    card_shape.fill.fore_color.rgb = bg_color
    if border_color:
        card_shape.line.color.rgb = border_color
        card_shape.line.width = border_width
    else:
        card_shape.line.fill.background()

def create_table(slide, rows, cols, left, top, width, height, col_widths, headers, data):
    tbl_shape = slide.shapes.add_table(rows, cols, left, top, width, height)
    tbl = tbl_shape.table
    for i, w in enumerate(col_widths):
        tbl.columns[i].width = w
    
    # Headers
    for c_idx, h in enumerate(headers):
        cell = tbl.cell(0, c_idx)
        cell.text = h
        cell.fill.solid()
        cell.fill.fore_color.rgb = CLR_BG_DARK
        for p in cell.text_frame.paragraphs:
            p.font.bold = True
            p.font.color.rgb = CLR_WHITE
            p.font.size = Pt(13)
            p.alignment = PP_ALIGN.LEFT
    
    # Data rows
    for r_idx, row in enumerate(data):
        row_bg = CLR_CARD_BG if r_idx % 2 == 0 else RGBColor(26, 39, 70)
        for c_idx, val in enumerate(row):
            cell = tbl.cell(r_idx + 1, c_idx)
            cell.text = str(val)
            cell.fill.solid()
            cell.fill.fore_color.rgb = row_bg
            for p in cell.text_frame.paragraphs:
                p.font.color.rgb = CLR_WHITE if c_idx == 0 or c_idx == 1 else RGBColor(226, 232, 240)
                p.font.size = Pt(11)
                if c_idx == 1:
                    p.font.bold = True
                    p.alignment = PP_ALIGN.CENTER
                else:
                    p.alignment = PP_ALIGN.LEFT
    return tbl_shape

def build_presentation():
    print(f"Loading base presentation: {SRC_PPTX}")
    prs = Presentation(SRC_PPTX)
    
    # ==========================================
    # SLIDE 1: Title Slide
    # ==========================================
    s1 = prs.slides[0]
    for s in s1.shapes:
        if s.name == "Text 2":
            s.text_frame.text = "Java Servlets 4.0  |  JSP & JSTL  |  JDBC  |  H2 & MySQL  |  Swing Desktop GUI"
            for p in s.text_frame.paragraphs:
                p.font.color.rgb = CLR_MUTED
                p.font.size = Pt(16)
        elif s.name == "TextBox 7":
            s.text_frame.text = "Team Info\nAditya Singh  25scse1180171\nManik  25scse1180216\nHarshit vats  25scse1180097"
            for p in s.text_frame.paragraphs:
                p.font.color.rgb = CLR_WHITE
                p.font.size = Pt(12)

    # ==========================================
    # SLIDE 2: What This Presentation Covers
    # ==========================================
    s2 = prs.slides[1]
    for s in s2.shapes:
        if s.name == "fact-number" and s.text_frame.text.strip() == "2":
            s.text_frame.text = "3" # 3 roles
        elif s.name == "fact-label" and "user roles" in s.text_frame.text.lower():
            s.text_frame.text = "user roles"
        elif s.name == "fact-number" and s.text_frame.text.strip() == "6":
            s.text_frame.text = "7" # 7 servlets
        elif s.name == "about-text":
            s.text_frame.text = (
                "FitPro is a full-stack fitness technology platform built in Java. "
                "It provides dual interfaces (modern Apple-inspired web app and Swing desktop client), "
                "dual-mode database (embedded zero-config H2 and MySQL 8), and role-based workflows for Members, Trainers, and Administrators.\n"
                "This presentation covers the problem, MVC architecture, pure Core Java depth, live screen walkthroughs, college rubric alignment, and viva demo."
            )
            for p in s.text_frame.paragraphs:
                p.font.size = Pt(12)
                p.font.color.rgb = CLR_WHITE

    # Update agenda titles, descs, and slide ranges
    s2_titles = [s for s in s2.shapes if s.name == "agenda-title"]
    s2_descs = [s for s in s2.shapes if s.name == "agenda-desc"]
    s2_slides = [s for s in s2.shapes if s.name == "agenda-slides"]

    new_titles = [
        "Problem and 3 roles",
        "Apple-inspired application UI",
        "Architecture & dual-mode DB",
        "Core Java implementation",
        "Evaluation rubrics & viva demo"
    ]
    new_descs = [
        "Why FitPro exists and what Member, Trainer & Admin can do",
        "Login, dashboard, workout catalogue, progress & admin",
        "Stack, MVC architecture, project structure, schema",
        "OOP, collections, threads, JDBC, servlets, security",
        "GUI & Web rubrics, server setup, live demo checklist"
    ]
    new_slides = [
        "Slides 3-4",
        "Slides 5-9",
        "Slides 10-13",
        "Slides 14-18",
        "Slides 19-24"
    ]

    for i in range(min(5, len(s2_titles))):
        s2_titles[i].text_frame.text = new_titles[i]
        for p in s2_titles[i].text_frame.paragraphs:
            p.font.bold = True
            p.font.size = Pt(12)
            p.font.color.rgb = CLR_WHITE

    for i in range(min(5, len(s2_descs))):
        s2_descs[i].text_frame.text = new_descs[i]
        for p in s2_descs[i].text_frame.paragraphs:
            p.font.size = Pt(10)
            p.font.color.rgb = CLR_MUTED

    for i in range(min(5, len(s2_slides))):
        s2_slides[i].text_frame.text = new_slides[i]
        for p in s2_slides[i].text_frame.paragraphs:
            p.font.size = Pt(11)
            p.font.bold = True
            p.font.color.rgb = CLR_ACCENT_BLUE

    # ==========================================
    # SLIDE 3: Problem & Solution
    # ==========================================
    s3 = prs.slides[2]
    for s in s3.shapes:
        if s.has_text_frame and "One web platform for members and trainers" in s.text_frame.text:
            s.text_frame.text = (
                "One unified platform for Members, Trainers, and Admins\n\n"
                "• Apple-inspired minimalist web UI + Java Swing desktop client\n"
                "• Dynamic polymorphic calorie expenditure calculation\n"
                "• Zero-configuration embedded H2 DB for immediate college evaluation\n"
                "• One-click live progress report export to CSV"
            )
            for p in s.text_frame.paragraphs:
                p.font.size = Pt(13)
                p.font.color.rgb = CLR_WHITE

    # ==========================================
    # SLIDE 4: Key Features by Role (Member, Trainer, Admin)
    # ==========================================
    s4 = prs.slides[3]
    # Update Slide 4 to show all 3 roles: Member, Trainer, Administrator
    # Reposition cards into 3 columns
    card_width = 3500000
    card_height = 4200000
    top_pos = 1463040
    
    # Member Card
    s_member_card = [s for s in s4.shapes if s.name == "Member-card"][0]
    s_member_card.left = 548640
    s_member_card.top = top_pos
    s_member_card.width = card_width
    s_member_card.height = card_height
    format_card(s_member_card)
    
    s_m_icon_circle = [s for s in s4.shapes if s.name == "icon-circle"][0]
    s_m_icon_circle.left = 750000
    s_m_icon_circle.top = 1680000
    
    s_m_icon = [s for s in s4.shapes if s.name == "icon"][0]
    s_m_icon.left = 910000
    s_m_icon.top = 1840000
    
    s_m_title = [s for s in s4.shapes if s.name == "Text 3"][0]
    s_m_title.left = 1600000
    s_m_title.top = 1800000
    s_m_title.width = 2300000
    
    s_m_body = [s for s in s4.shapes if s.name == "Text 4"][0]
    s_m_body.left = 750000
    s_m_body.top = 2600000
    s_m_body.width = 3100000
    s_m_body.text_frame.text = (
        "• Secure PBKDF2 session authentication\n"
        "• Browse & filter workouts by type\n"
        "• 1-Click enrollment with duplicate check\n"
        "• Log minutes & weight for calorie math\n"
        "• Interactive charts & instant CSV export"
    )
    for p in s_m_body.text_frame.paragraphs:
        p.font.size = Pt(13)
        p.font.color.rgb = CLR_WHITE

    # Trainer Card
    s_trainer_card = [s for s in s4.shapes if s.name == "Trainer-card"][0]
    s_trainer_card.left = 4348640
    s_trainer_card.top = top_pos
    s_trainer_card.width = card_width
    s_trainer_card.height = card_height
    format_card(s_trainer_card)
    
    s_t_icon_circle = [s for s in s4.shapes if s.name == "icon-circle"][1]
    s_t_icon_circle.left = 4550000
    s_t_icon_circle.top = 1680000
    
    s_t_icon = [s for s in s4.shapes if s.name == "icon"][1]
    s_t_icon.left = 4710000
    s_t_icon.top = 1840000
    
    s_t_title = [s for s in s4.shapes if s.name == "Text 7"][0]
    s_t_title.left = 5400000
    s_t_title.top = 1800000
    s_t_title.width = 2300000
    
    s_t_body = [s for s in s4.shapes if s.name == "Text 8"][0]
    s_t_body.left = 4550000
    s_t_body.top = 2600000
    s_t_body.width = 3100000
    s_t_body.text_frame.text = (
        "• Dedicated Trainer Studio interface\n"
        "• Publish custom programs with MET intensity\n"
        "• Choose Cardio, Strength, or Yoga\n"
        "• Instant catalogue availability for all members\n"
        "• Track program enrollment statistics"
    )
    for p in s_t_body.text_frame.paragraphs:
        p.font.size = Pt(13)
        p.font.color.rgb = CLR_WHITE

    # Add 3rd Card: Administrator
    s_admin_card = s4.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, 8148640, top_pos, card_width, card_height)
    format_card(s_admin_card)
    
    # Admin Title
    tb_admin_title = s4.shapes.add_textbox(8350000, 1800000, 3100000, 457200)
    tb_admin_title.text_frame.text = "Administrator"
    p = tb_admin_title.text_frame.paragraphs[0]
    p.font.size = Pt(22)
    p.font.bold = True
    p.font.color.rgb = CLR_WHITE
    
    # Admin Body
    tb_admin_body = s4.shapes.add_textbox(8350000, 2600000, 3100000, 2651760)
    tb_admin_body.text_frame.text = (
        "• Restricted /admin portal via AuthFilter\n"
        "• Platform metrics: total users, workouts, logs\n"
        "• User management table with role badges\n"
        "• System governance: delete inappropriate workouts\n"
        "• Full auditability and role enforcement"
    )
    for p in tb_admin_body.text_frame.paragraphs:
        p.font.size = Pt(13)
        p.font.color.rgb = CLR_WHITE

    # Shared footer
    s_footer = [s for s in s4.shapes if s.name == "Text 9"][0]
    s_footer.text_frame.text = "Shared Foundation: PBKDF2-HMAC-SHA256 password hashing, session regeneration, input sanitization, and AuthFilter access control."
    for p in s_footer.text_frame.paragraphs:
        p.font.size = Pt(12)
        p.font.color.rgb = CLR_MUTED

    # ==========================================
    # SLIDE 5: Screen 1: Login & Access Control
    # ==========================================
    s5 = prs.slides[4]
    replace_image(s5, "screenshot", os.path.join(SC_DIR, "01_login.png"))
    for s in s5.shapes:
        if s.name == "Title 1":
            s.text_frame.text = "Screen 1: Authentication & Access Control"
        elif s.name == "path-text":
            s.text_frame.text = (
                "Behind this screen:  POST /login\n"
                "login.jsp  >  LoginServlet  >  AuthService  >  UserDAO  >  users table"
            )
            for p in s.text_frame.paragraphs:
                p.font.size = Pt(13)
                p.font.color.rgb = CLR_WHITE
        elif s.name == "explain-items":
            s.text_frame.text = (
                "1   Brand navigation: Apple-inspired header with logo and direct routing.\n"
                "2   1-Click demo logins: Instant credential quick-fill for Member, Trainer & Admin.\n"
                "3   Input fields: Subtle borders with visible focus state and client validation.\n"
                "4   Sign In CTA: Triggers PBKDF2 hash verification and session regeneration.\n"
                "5   Account registration: Direct route to register as a new Member or Trainer."
            )
            for p in s.text_frame.paragraphs:
                p.font.size = Pt(13)
                p.font.color.rgb = CLR_WHITE

    # ==========================================
    # SLIDE 6: Screen 2: Member Dashboard
    # ==========================================
    s6 = prs.slides[5]
    replace_image(s6, "screenshot", os.path.join(SC_DIR, "03_member_dashboard.png"))
    for s in s6.shapes:
        if s.name == "Title 1":
            s.text_frame.text = "Screen 2: Member Dashboard & Analytics"
        elif s.name == "path-text":
            s.text_frame.text = (
                "Behind this screen:  GET /app/dashboard\n"
                "DashboardServlet  >  ProgressService, WorkoutService  >  dashboard.jsp"
            )
            for p in s.text_frame.paragraphs:
                p.font.size = Pt(13)
                p.font.color.rgb = CLR_WHITE
        elif s.name == "explain-items":
            s.text_frame.text = (
                "1   Personalized banner: Greeting and role badge (MEMBER) loaded from session.\n"
                "2   KPI metric cards: Completed sessions, active minutes, calories, enrollments.\n"
                "3   Weekly activity chart: Visual calorie expenditure trend across workouts.\n"
                "4   My Enrolled Workouts: Real-time cards showing active programs and duration.\n"
                "5   Quick action links: 1-click shortcuts to Log Activity or Explore Catalogue."
            )
            for p in s.text_frame.paragraphs:
                p.font.size = Pt(13)
                p.font.color.rgb = CLR_WHITE

    # ==========================================
    # SLIDE 7: Screen 3: Workout Catalogue & Search
    # ==========================================
    s7 = prs.slides[6]
    replace_image(s7, "screenshot", os.path.join(SC_DIR, "04_workout_catalogue.png"))
    for s in s7.shapes:
        if s.name == "Title 1":
            s.text_frame.text = "Screen 3: Workout Catalogue & Dynamic Search"
        elif s.name == "path-text":
            s.text_frame.text = (
                "Behind this screen:  GET and POST /app/workouts\n"
                "WorkoutServlet  >  WorkoutService  >  WorkoutDAO, EnrollmentDAO  >  workouts.jsp"
            )
            for p in s.text_frame.paragraphs:
                p.font.size = Pt(13)
                p.font.color.rgb = CLR_WHITE
        elif s.name == "explain-items":
            s.text_frame.text = (
                "1   Category filter chips: Instant filtering by All, Cardio, Strength, Yoga, HIIT.\n"
                "2   Dynamic search bar: Real-time filtering by program title or trainer name.\n"
                "3   Workout cards: Difficulty badge, duration, and projected calorie burn.\n"
                "4   Enroll Now CTA: Persists enrollment in DB and triggers async notification.\n"
                "5   Enrolled state: Turns green once enrolled; duplicate enrollments blocked."
            )
            for p in s.text_frame.paragraphs:
                p.font.size = Pt(13)
                p.font.color.rgb = CLR_WHITE

    # ==========================================
    # SLIDE 8: Technology Stack & Dual-Mode DB
    # ==========================================
    s8 = prs.slides[7]
    for s in s8.shapes:
        if s.name == "Text 12":
            s.text_frame.text = "Dual-mode: H2 in-memory (zero config) + MySQL 8 via JDBC"
        elif s.name == "Text 20":
            s.text_frame.text = "Apache Tomcat 9 (javax.servlet) + Maven Cargo embedded"
        elif s.name == "Text 22":
            s.text_frame.text = (
                "Architected to cover both college rubrics completely: Core Java, Servlets 4.0, "
                "JDBC database integration, dual-mode persistence, and desktop Java Swing GUI."
            )

    # ==========================================
    # SLIDE 9: Layered MVC Architecture
    # ==========================================
    s9 = prs.slides[8]
    for s in s9.shapes:
        if s.name == "Text 14" and "MySQL" in s.text_frame.text:
            s.text_frame.text = "MySQL / H2\nfitpro database"
        elif s.name == "Text 15" and "servlet" in s.text_frame.text:
            s.text_frame.text = "com.fitpro.servlet\n(7 Servlets)"
        elif s.name == "Text 20":
            s.text_frame.text = "Every /app/* and /admin/* request is checked for active session and role permissions before reaching a servlet."

    # ==========================================
    # SLIDE 10: Project Structure & Code Metrics
    # ==========================================
    s10 = prs.slides[9]
    for s in s10.shapes:
        if s.name == "Text 13" and s.text_frame.text.strip() == "6":
            s.text_frame.text = "7" # 7 Servlets
        elif s.name == "Text 2":
            s.text_frame.text = (
                "fitpro/\n"
                "|-- pom.xml\n"
                "|-- README.md\n"
                "|-- database/schema.sql\n"
                "`-- src/\n"
                "    |-- main/java/com/fitpro/\n"
                "    |   |-- model/     entities (OOP)\n"
                "    |   |-- dao/       JDBC classes\n"
                "    |   |-- service/   business rules\n"
                "    |   |-- servlet/   7 controllers\n"
                "    |   |-- filter/    AuthFilter\n"
                "    |   |-- listener/  lifecycle\n"
                "    |   |-- gui/       Swing client\n"
                "    |   `-- util/      DBConnection\n"
                "    `-- test/java/ (39 JUnit tests)"
            )
            for p in s.text_frame.paragraphs:
                p.font.size = Pt(11)
                p.font.color.rgb = CLR_WHITE

    # ==========================================
    # SLIDE 11: Database Design
    # ==========================================
    s11 = prs.slides[10]
    for s in s11.shapes:
        if s.name == "Text 4" and "MEMBER / TRAINER" in s.text_frame.text:
            s.text_frame.text = "id (PK)\nfull_name\nemail (UNIQUE)\npassword_hash\nrole: MEMBER / TRAINER / ADMIN"

    # ==========================================
    # SLIDE 12: OOP Polymorphism
    # ==========================================
    s12 = prs.slides[11]
    # Add Admin to User hierarchy
    tb_admin_oop = s12.shapes.add_textbox(3800000, 3100000, 1500000, 450000)
    tb_admin_oop.text_frame.text = "Admin"
    p = tb_admin_oop.text_frame.paragraphs[0]
    p.font.size = Pt(14)
    p.font.bold = True
    p.font.color.rgb = CLR_WHITE

    # ==========================================
    # SLIDE 14: Multithreading
    # ==========================================
    s14 = prs.slides[13]
    for s in s14.shapes:
        if s.name == "Text 3":
            s.text_frame.text = (
                "Registration returns instantly\n"
                "ExecutorService with 3 worker daemon threads\n"
                "Shared counter guarded by synchronized\n"
                "SwingWorker executes DB tasks off the Swing EDT\n"
                "Pool shut down cleanly by AppContextListener"
            )

    # ==========================================
    # SLIDE 15: JDBC and Servlet Integration
    # ==========================================
    s15 = prs.slides[14]
    for s in s15.shapes:
        if s.name == "Text 4":
            s.text_frame.text = (
                "DBConnection singleton loads driver once\n"
                "Dual-mode: Embedded H2 (zero config) or MySQL 8\n"
                "100% PreparedStatement for every query\n"
                "try-with-resources closes connections deterministically\n"
                "Four DAOs: User, Workout, Enrollment, Progress\n"
                "7 Servlets mapped with clean URL routing"
            )

    # ==========================================
    # RECONFIGURE SLIDE 17 (Old 33 marks) -> RUBRIC 1: GUI Based (17 Marks)
    # ==========================================
    s17 = prs.slides[16]
    for s in s17.shapes:
        if s.name == "Text 0":
            s.text_frame.text = "Java GUI Based Projects Marking Rubric — 17 Marks"
        elif s.name == "Text 2":
            s.text_frame.text = "17" # 17 marks
        elif s.name == "Text 3":
            s.text_frame.text = "marks demonstrated"
        elif s.name == "Text 5":
            s.text_frame.text = (
                "Demonstration Checklist:\n"
                "• 7 Servlets with full HTTP lifecycle\n"
                "• Desktop Java Swing GUI (FitProGUI)\n"
                "• Zero-config H2 embedded database\n"
                "• Asynchronous notification worker\n"
                "• Clean DAO & try-with-resources"
            )
            for p in s.text_frame.paragraphs:
                p.font.size = Pt(12)
                p.font.color.rgb = CLR_WHITE
        elif s.has_table:
            table = s.table
            headers = ["Evaluation Criterion", "Marks", "FitPro Evidence & Demonstration"]
            data = [
                ["1. Servlet Implementation", "10", "7 Servlets (Login, Register, Logout, Dashboard, Workout, Progress, Admin) managing full HTTP lifecycle, sessions & AuthFilter"],
                ["2. Code Quality & Execution", "5", "Clean MVC layering, try-with-resources, connection pooling, and desktop Swing GUI (FitProGUI) with SwingWorker"],
                ["3. Innovation / Extra Effort", "2", "Dual-Interface (Web + Desktop Swing GUI), dual-mode DB (in-memory H2 + MySQL), and PBKDF2 cryptography"]
            ]
            # Clear and populate table
            for c_idx, h in enumerate(headers):
                cell = table.cell(0, c_idx)
                cell.text = h
                cell.fill.solid()
                cell.fill.fore_color.rgb = CLR_BG_DARK
                for p in cell.text_frame.paragraphs:
                    p.font.bold = True
                    p.font.size = Pt(12)
                    p.font.color.rgb = CLR_WHITE
            for r_idx, row in enumerate(data):
                for c_idx, val in enumerate(row):
                    cell = table.cell(r_idx + 1, c_idx)
                    cell.text = val
                    cell.fill.solid()
                    cell.fill.fore_color.rgb = CLR_CARD_BG if r_idx % 2 == 0 else RGBColor(26, 39, 70)
                    for p in cell.text_frame.paragraphs:
                        p.font.size = Pt(11)
                        p.font.color.rgb = CLR_WHITE
                        if c_idx == 1:
                            p.font.bold = True
                            p.alignment = PP_ALIGN.CENTER
            # Blank out row 4 if exists
            if len(table.rows) > 4:
                table.cell(4, 0).text = "Total"
                table.cell(4, 1).text = "17 / 17"
                table.cell(4, 2).text = "100% Viva Rubric Requirement Met"
                for c_idx in range(3):
                    cell = table.cell(4, c_idx)
                    cell.fill.solid()
                    cell.fill.fore_color.rgb = CLR_ACCENT_BLUE
                    for p in cell.text_frame.paragraphs:
                        p.font.bold = True
                        p.font.color.rgb = CLR_WHITE

    # ==========================================
    # SLIDE 18 (Now Slide 23): Future Scope & Project Conclusion
    # ==========================================
    s18 = prs.slides[17]
    for s in s18.shapes:
        if s.name == "Text 0":
            s.text_frame.text = "Future Scope & Project Conclusion"

    # Remove old step circles and step text boxes
    shapes_to_remove = [
        s for s in s18.shapes
        if s.name in ["step-circle", "Text 2", "Text 3", "Text 5", "Text 6", "Text 8", "Text 9", "Text 11", "Text 12"]
    ]
    for s in shapes_to_remove:
        sp = s._element
        sp.getparent().remove(sp)

    # Add left side: Project Milestones Card
    milestone_card = s18.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, 548640, 1463040, 5600000, 4572000)
    format_card(milestone_card)
    
    tb_ms_h = s18.shapes.add_textbox(800000, 1650000, 5100000, 450000)
    tb_ms_h.text_frame.text = "Project Summary & Milestones"
    p = tb_ms_h.text_frame.paragraphs[0]
    p.font.size = Pt(18)
    p.font.bold = True
    p.font.color.rgb = CLR_WHITE
    
    tb_ms_b = s18.shapes.add_textbox(800000, 2250000, 5100000, 3600000)
    tb_ms_b.text_frame.text = (
        "• 100% Viva Rubric Coverage across GUI & Web requirements\n\n"
        "• 7 Java Servlets with complete HTTP lifecycle & AuthFilter\n\n"
        "• Dual-Interface: Apple-inspired Web + Swing Desktop GUI\n\n"
        "• Dual-Mode DB: Zero-config embedded H2 + MySQL 8 production\n\n"
        "• 39 Automated JUnit test suites ensuring zero regression\n\n"
        "• Live polymorphic calorie calculation engine (MET formulas)\n\n"
        "• Instant one-click progress report export to CSV format"
    )
    for p in tb_ms_b.text_frame.paragraphs:
        p.font.size = Pt(12)
        p.font.color.rgb = CLR_WHITE

    # Update right side: Future Scope Card
    for s in s18.shapes:
        if s.name == "future-card":
            s.left = 6450000
            s.top = 1463040
            s.width = 5160000
            s.height = 4572000
            format_card(s)
        elif s.name == "Text 14":
            s.left = 6700000
            s.top = 1650000
            s.text_frame.text = "Future Roadmap & Industry Evolution"
            p = s.text_frame.paragraphs[0]
            p.font.size = Pt(18)
            p.font.bold = True
            p.font.color.rgb = CLR_WHITE
        elif s.name == "Text 15":
            s.left = 6700000
            s.top = 2250000
            s.width = 4650000
            s.text_frame.text = (
                "• RESTful API for iOS & Android companion mobile apps\n\n"
                "• Wearable fitness integration (Apple HealthKit & Google Fit)\n\n"
                "• Live virtual video streaming for interactive trainer sessions\n\n"
                "• Machine learning workout recommendation & injury alert engine\n\n"
                "• Production cloud deployment with automated CI/CD pipeline"
            )
            for p in s.text_frame.paragraphs:
                p.font.size = Pt(12)
                p.font.color.rgb = CLR_WHITE

    # ==========================================
    # SLIDE 19: Thank You
    # ==========================================
    s19 = prs.slides[18]
    # Update thank you subtitle
    tb_thank_info = s19.shapes.add_textbox(2000000, 4800000, 8000000, 800000)
    tb_thank_info.text_frame.text = (
        "Aditya Singh (25scse1180171)   |   Manik (25scse1180216)   |   Harshit vats (25scse1180097)\n"
        "FitPro — Online Fitness Training Platform  •  B.Tech Java Project Evaluation"
    )
    for p in tb_thank_info.text_frame.paragraphs:
        p.alignment = PP_ALIGN.CENTER
        p.font.size = Pt(14)
        p.font.color.rgb = CLR_MUTED

    print("Base slides updated successfully. Now creating new custom slides...")

    # ==========================================
    # NEW SLIDE A: Screen 4: Progress Tracking & CSV Export
    # ==========================================
    new_s4 = prs.slides.add_slide(prs.slide_layouts[2])
    list(new_s4.placeholders)[0].text = "Screen 4: Progress Tracking & CSV Export"
    
    # Add screenshot
    new_s4.shapes.add_picture(
        os.path.join(SC_DIR, "05_member_progress.png"),
        548640, 1508760, 7040880, 3322252
    )
    
    # Add path card & text
    path_card = new_s4.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, 548640, 5059612, 7040880, 1066867)
    format_card(path_card)
    tb_path = new_s4.shapes.add_textbox(822959, 5196772, 6492240, 792547)
    tb_path.text_frame.text = (
        "Behind this screen:  GET and POST /app/progress\n"
        "ProgressServlet  >  ProgressService  >  ProgressDAO  >  progress_logs table"
    )
    for p in tb_path.text_frame.paragraphs:
        p.font.size = Pt(13)
        p.font.color.rgb = CLR_WHITE
        p.font.bold = True
    
    # Add explain card & text
    exp_card = new_s4.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, 7863840, 1508760, 3794760, 4617720)
    format_card(exp_card)
    
    tb_exp_h = new_s4.shapes.add_textbox(8138160, 1737360, 3246120, 411480)
    tb_exp_h.text_frame.text = "Screen walkthrough"
    p = tb_exp_h.text_frame.paragraphs[0]
    p.font.size = Pt(18)
    p.font.bold = True
    p.font.color.rgb = CLR_WHITE
    
    tb_exp_b = new_s4.shapes.add_textbox(8138160, 2286000, 3246120, 3657600)
    tb_exp_b.text_frame.text = (
        "1   Session logging form: Select enrolled program, input minutes & body weight in kg.\n\n"
        "2   Polymorphic calorie math: Computes exact burn using dynamic MET formulas.\n\n"
        "3   Metric aggregations: Real-time cumulative minutes and calories burned update.\n\n"
        "4   Session history table: Chronological audit table of completed workout sessions.\n\n"
        "5   1-Click CSV export: Instant download of fitpro-progress-report.csv for analysis."
    )
    for p in tb_exp_b.text_frame.paragraphs:
        p.font.size = Pt(13)
        p.font.color.rgb = CLR_WHITE

    # ==========================================
    # NEW SLIDE B: Screen 5: Trainer Studio & Admin Portal
    # ==========================================
    new_s5 = prs.slides.add_slide(prs.slide_layouts[2])
    list(new_s5.placeholders)[0].text = "Screen 5: Trainer Studio & Admin Portal"
    
    # Add 2 screenshots: Trainer top, Admin bottom
    new_s5.shapes.add_picture(
        os.path.join(SC_DIR, "06_trainer_studio.png"),
        548640, 1508760, 7040880, 2200000
    )
    new_s5.shapes.add_picture(
        os.path.join(SC_DIR, "07_admin_portal.png"),
        548640, 3850000, 7040880, 2200000
    )
    
    # Add explain card & text
    exp_card5 = new_s5.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, 7863840, 1508760, 3794760, 4540000)
    format_card(exp_card5)
    
    tb_exp5_h = new_s5.shapes.add_textbox(8138160, 1737360, 3246120, 411480)
    tb_exp5_h.text_frame.text = "Role-Based Governance"
    p = tb_exp5_h.text_frame.paragraphs[0]
    p.font.size = Pt(18)
    p.font.bold = True
    p.font.color.rgb = CLR_WHITE
    
    tb_exp5_b = new_s5.shapes.add_textbox(8138160, 2250000, 3246120, 3657600)
    tb_exp5_b.text_frame.text = (
        "1   Trainer Studio: Form to author new workouts with category, intensity, and duration.\n\n"
        "2   Catalogue sync: Published workouts immediately appear across all member accounts.\n\n"
        "3   Admin platform KPIs: Real-time metrics on total members, trainers, and logs.\n\n"
        "4   User management table: Complete user roster with role badges & creation dates.\n\n"
        "5   AuthFilter protection: Server enforces role boundaries on /admin/* and /app/*."
    )
    for p in tb_exp5_b.text_frame.paragraphs:
        p.font.size = Pt(13)
        p.font.color.rgb = CLR_WHITE

    # ==========================================
    # NEW SLIDE C: RUBRIC 2: Java Web Based Projects (17 Marks)
    # ==========================================
    new_rubric2 = prs.slides.add_slide(prs.slide_layouts[2])
    list(new_rubric2.placeholders)[0].text = "Java Web Based Projects Marking Rubric — 17 Marks"
    
    col_widths = [3108960, 822960, 3749040]
    headers = ["Evaluation Criterion", "Marks", "FitPro Evidence & Demonstration"]
    data = [
        ["1. Code Quality & Testing", "10", "39 automated JUnit & Mockito tests covering DAOs, Services, and Password Hashing; zero compiler warnings; clean code conventions"],
        ["2. Teamwork & Collaboration", "5", "Aditya Singh (Architecture, Auth, Admin), Manik (Service Layer, DAOs, Multithreading & Tests), Harshit Vats (UI JSPs, Apple CSS, Swing GUI)"],
        ["3. Innovation / Extra Effort", "2", "Apple-inspired minimal design system, live CSV Progress Report export, and zero-dependency embedded Tomcat execution (mvn cargo:run)"],
        ["Total", "17 / 17", "100% Viva Rubric Requirement Met with Full Working Evidence"]
    ]
    tbl_shape = create_table(new_rubric2, 5, 3, 548640, 1463040, 7680960, 3886200, col_widths, headers, data)
    
    # Highlight total row in blue
    tbl = tbl_shape.table
    for c in range(3):
        tbl.cell(4, c).fill.solid()
        tbl.cell(4, c).fill.fore_color.rgb = CLR_ACCENT_BLUE
        for p in tbl.cell(4, c).text_frame.paragraphs:
            p.font.bold = True
            p.font.color.rgb = CLR_WHITE
    
    # Right-side Marks Card
    marks_card = new_rubric2.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, 8595360, 1463040, 3108960, 2200000)
    format_card(marks_card)
    tb_m_num = new_rubric2.shapes.add_textbox(8595360, 1600000, 3108960, 1100000)
    tb_m_num.text_frame.text = "17"
    p = tb_m_num.text_frame.paragraphs[0]
    p.font.size = Pt(56)
    p.font.bold = True
    p.font.color.rgb = CLR_WHITE
    p.alignment = PP_ALIGN.CENTER
    
    tb_m_lbl = new_rubric2.shapes.add_textbox(8595360, 2750000, 3108960, 450000)
    tb_m_lbl.text_frame.text = "marks demonstrated"
    p = tb_m_lbl.text_frame.paragraphs[0]
    p.font.size = Pt(14)
    p.font.color.rgb = CLR_MUTED
    p.alignment = PP_ALIGN.CENTER
    
    # Right-side Team Matrix Card
    team_card = new_rubric2.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, 8595360, 3900000, 3108960, 2150000)
    format_card(team_card)
    tb_t_info = new_rubric2.shapes.add_textbox(8750000, 4050000, 2800000, 1900000)
    tb_t_info.text_frame.text = (
        "Team Ownership Matrix:\n"
        "• Aditya Singh (25scse1180171):\n"
        "  Architecture, Security, Admin\n"
        "• Manik (25scse1180216):\n"
        "  Service layer, DAOs, Testing\n"
        "• Harshit vats (25scse1180097):\n"
        "  JSP Views, Apple UI, Swing GUI\n"
        "• Full Git branches & commit history"
    )
    for p in tb_t_info.text_frame.paragraphs:
        p.font.size = Pt(11)
        p.font.color.rgb = CLR_WHITE

    # ==========================================
    # NEW SLIDE D: Local Server Setup Guide
    # ==========================================
    new_setup = prs.slides.add_slide(prs.slide_layouts[2])
    list(new_setup.placeholders)[0].text = "Local Server Setup & Execution Guide"
    
    # Left Card: Zero Config Quick Start
    setup_card_l = new_setup.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, 548640, 1463040, 5394960, 4600000)
    format_card(setup_card_l)
    
    tb_sl_h = new_setup.shapes.add_textbox(800000, 1650000, 4900000, 450000)
    tb_sl_h.text_frame.text = "Zero-Configuration Webapp Launch"
    p = tb_sl_h.text_frame.paragraphs[0]
    p.font.size = Pt(18)
    p.font.bold = True
    p.font.color.rgb = CLR_WHITE
    
    tb_sl_b = new_setup.shapes.add_textbox(800000, 2200000, 4900000, 3700000)
    tb_sl_b.text_frame.text = (
        "1. Verify System Prerequisites:\n"
        "   java -version  (JDK 11 or higher)\n"
        "   mvn -v         (Apache Maven 3.8+)\n\n"
        "2. Clean & Package WAR File:\n"
        "   cd fitpro\n"
        "   mvn clean package\n\n"
        "3. Launch Embedded Tomcat 9 via Cargo:\n"
        "   mvn cargo:run\n"
        "   (Starts Tomcat 9 on port 8080 automatically)\n\n"
        "4. Open Live Platform in Browser:\n"
        "   http://localhost:8080/fitpro/\n"
        "   (Zero database configuration needed: H2 embedded)"
    )
    for p in tb_sl_b.text_frame.paragraphs:
        p.font.size = Pt(13)
        p.font.color.rgb = CLR_WHITE

    # Right Card: GUI, Testing & Troubleshooting
    setup_card_r = new_setup.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, 6263640, 1463040, 5394960, 4600000)
    format_card(setup_card_r)
    
    tb_sr_h = new_setup.shapes.add_textbox(6500000, 1650000, 4900000, 450000)
    tb_sr_h.text_frame.text = "Desktop GUI, Tests & Troubleshooting"
    p = tb_sr_h.text_frame.paragraphs[0]
    p.font.size = Pt(18)
    p.font.bold = True
    p.font.color.rgb = CLR_WHITE
    
    tb_sr_b = new_setup.shapes.add_textbox(6500000, 2200000, 4900000, 3700000)
    tb_sr_b.text_frame.text = (
        "• Launch Desktop Swing GUI Client:\n"
        "   mvn compile exec:java\n"
        "   (Opens FitProGUI desktop window with CardLayout)\n\n"
        "• Execute 39 Automated Test Suites:\n"
        "   mvn clean test\n"
        "   (Runs all DAO, Service, and Auth unit tests)\n\n"
        "• Dual-Mode Database Switching:\n"
        "   Default: In-memory H2 (Zero install needed)\n"
        "   Production: Set DB_HOST & credentials in db.properties\n\n"
        "• Port 8080 Conflict Resolution:\n"
        "   Stop-Process -Id (Get-NetTCPConnection -LocalPort 8080)\n"
        "   .OwningProcess -Force"
    )
    for p in tb_sr_b.text_frame.paragraphs:
        p.font.size = Pt(13)
        p.font.color.rgb = CLR_WHITE

    # ==========================================
    # NEW SLIDE E: Live Viva Demo Walkthrough
    # ==========================================
    new_demo = prs.slides.add_slide(prs.slide_layouts[2])
    list(new_demo.placeholders)[0].text = "Live Viva Demo Walkthrough & Checklist"
    
    # 8-step demo checklist card
    demo_card = new_demo.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, 548640, 1463040, 11064240, 4600000)
    format_card(demo_card)
    
    tb_demo_b = new_demo.shapes.add_textbox(800000, 1650000, 10500000, 4200000)
    tb_demo_b.text_frame.text = (
        "Step-by-Step Viva Demonstration Script for the Evaluation Committee:\n\n"
        "1. Minimal Landing Page: Open http://localhost:8080/fitpro/ to show Apple-inspired minimalism and typography.\n"
        "2. Member Authentication: Click 'Sign In' and use 1-click Quick-Fill for demo@fitpro.com / Demo@123.\n"
        "3. Member Dashboard: Demonstrate KPI cards (completed sessions, calories), enrolled workouts & activity chart.\n"
        "4. Workout Catalogue: Demonstrate live search and category chips (Cardio, Strength, Yoga), click 'Enroll Now'.\n"
        "5. Session Logging: Log a 45-min workout with 70 kg weight; show dynamic polymorphic calorie calculation formula.\n"
        "6. CSV Report Export: Click 'Export Progress Report (CSV)' to instantly download spreadsheet data.\n"
        "7. Trainer Studio: Sign in as trainer (trainer@fitpro.com) and publish a new workout program live.\n"
        "8. Admin Portal: Sign in as admin (admin@fitpro.com) to view platform analytics and user management table.\n"
        "9. Desktop Swing GUI: Run 'mvn compile exec:java' to demonstrate the standalone desktop client."
    )
    for i, p in enumerate(tb_demo_b.text_frame.paragraphs):
        if i == 0:
            p.font.size = Pt(15)
            p.font.bold = True
            p.font.color.rgb = CLR_ACCENT_BLUE
        else:
            p.font.size = Pt(13)
            p.font.color.rgb = CLR_WHITE

    # ==========================================
    # REORDER SLIDES TO FINAL LOGICAL SEQUENCE
    # ==========================================
    # Let's inspect the slide count and IDs
    print(f"Total slides before reordering: {len(prs.slides)}")
    
    # The slide IDs currently in prs.slides:
    # 0..18 are the original 19 slides
    # 19: Screen 4 (Progress)
    # 20: Screen 5 (Trainer & Admin)
    # 21: Rubric 2 (Web Based)
    # 22: Local Server Setup
    # 23: Live Viva Demo
    
    sldIdLst = prs.slides._sldIdLst
    s_ids = list(sldIdLst)
    
    # Desired order:
    # 0: Slide 1 (Title)
    # 1: Slide 2 (Overview)
    # 2: Slide 3 (Problem/Solution)
    # 3: Slide 4 (Roles)
    # 4: Slide 5 (Screen 1 Login)
    # 5: Slide 6 (Screen 2 Dashboard)
    # 6: Slide 7 (Screen 3 Catalogue)
    # 7: New Slide A (Screen 4 Progress) -> index 19
    # 8: New Slide B (Screen 5 Trainer & Admin) -> index 20
    # 9: Slide 8 (Tech Stack) -> index 7
    # 10: Slide 9 (MVC) -> index 8
    # 11: Slide 10 (Project Structure) -> index 9
    # 12: Slide 11 (Database Design) -> index 10
    # 13: Slide 12 (OOP) -> index 11
    # 14: Slide 13 (Collections) -> index 12
    # 15: Slide 14 (Threads) -> index 13
    # 16: Slide 15 (JDBC) -> index 14
    # 17: Slide 16 (Security) -> index 15
    # 18: Slide 17 (Rubric 1 GUI) -> index 16
    # 19: New Slide C (Rubric 2 Web) -> index 21
    # 20: New Slide D (Server Setup) -> index 22
    # 21: New Slide E (Live Demo) -> index 23
    # 22: Slide 18 (Future Scope) -> index 17
    # 23: Slide 19 (Thank You) -> index 18
    
    ordered_indices = [
        0, 1, 2, 3, 4, 5, 6,
        19, 20,
        7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
        21, 22, 23,
        17, 18
    ]
    
    # Apply reordering
    sldIdLst[:] = [s_ids[orig_idx] for orig_idx in ordered_indices]
        
    print(f"Reordered {len(ordered_indices)} slides successfully!")
    
    # Update slide numbers
    for idx, slide in enumerate(prs.slides):
        for s in slide.shapes:
            if s.name == "Slide Number Placeholder 0":
                s.text_frame.text = str(idx + 1)
                for p in s.text_frame.paragraphs:
                    p.font.size = Pt(11)
                    p.font.color.rgb = CLR_MUTED

    print(f"Saving final presentation to: {OUT_PPTX}")
    prs.save(OUT_PPTX)
    print("DONE! Presentation built successfully!")

if __name__ == '__main__':
    build_presentation()
