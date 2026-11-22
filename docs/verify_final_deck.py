from pptx import Presentation

prs = Presentation(r'D:\fitpro\fitpro\docs\FitPro_Final_Presentation.pptx')
print(f"Total slides in FitPro_Final_Presentation.pptx: {len(prs.slides)}\n")

docker_found = []
for idx, slide in enumerate(prs.slides):
    first_title = "No Title"
    all_texts = []
    has_image = False
    has_table = False
    for s in slide.shapes:
        if s.shape_type == 13:
            has_image = True
        if s.has_table:
            has_table = True
        if s.has_text_frame:
            t = s.text_frame.text.strip()
            if t:
                all_texts.append(t)
                if 'docker' in t.lower() or 'compose' in t.lower() or 'kubernetes' in t.lower():
                    docker_found.append((idx + 1, t))
    if all_texts:
        first_title = all_texts[0].split('\n')[0]
    badge = f"[IMG]" if has_image else ""
    badge += f"[TBL]" if has_table else ""
    print(f"Slide {idx+1:2d}: {first_title[:55]:<55} {badge}")

print("\n--- Docker Check ---")
if docker_found:
    print(f"WARNING: Docker keywords found: {docker_found}")
else:
    print("SUCCESS: Zero Docker or DevOps references found!")
