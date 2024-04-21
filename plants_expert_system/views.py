from aima.logic import *
from aima.utils import *
import json

from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt


@csrf_exempt
def getIssue(request):
    data = json.loads(request.body)
    door = data.get('door')
    water = data.get('water')
    leaf = data.get('leaf')

    KB = FolKB()
    KB.tell(expr(f'{door}(plant)'))
    KB.tell(expr(f'{water}(plant)'))
    KB.tell(expr(f'{leaf}(plant)'))

    KB.tell(expr('Indoor(x) & Always(x) & Darkroot(x) ==> Root_rot_over_watering(x)'))
    KB.tell(expr('Indoor(x) & Always(x) & Wilting(x) ==> Lack_of_fertilizing(x)'))
    KB.tell(expr('Indoor(x) & Always(x) & Yellow_leaves(x) ==> Not_exposed_to_lighting(x)'))
    KB.tell(expr('Indoor(x) & Always(x) & Fungus(x) ==> Lack_of_ventilation_humidity(x)'))

    KB.tell(expr('Indoor(x) & Sometimes(x) & Darkroot(x) ==> Chemical_exposure(x)'))
    KB.tell(expr('Indoor(x) & Sometimes(x) & Wilting(x) ==> Under_watering_lack_of_fertilizing (x)'))
    KB.tell(expr('Indoor(x) & Sometimes(x) & Yellow_leaves(x) ==> Under_watering_not_exposed_to_lighting(x)'))
    KB.tell(expr('Indoor(x) & Sometimes(x) & Fungus(x) ==> Lack_of_ventilation(x)'))

    KB.tell(expr('Outdoor(x) & Always(x) & Darkroot(x) ==> Improper_drainage(x)'))
    KB.tell(expr('Outdoor(x) & Always(x) & Wilting(x) ==> Overwatering (x)'))
    KB.tell(expr('Outdoor(x) & Always(x) & Yellow_leaves(x) ==> Lack_of_nutrients_particularly_nitrogen(x)'))

    KB.tell(expr('Outdoor(x) & Sometimes(x) & Darkroot(x) ==> Chemical_exposure_improper_drainage(x)'))
    KB.tell(expr('Outdoor(x) & Sometimes(x) & Wilting(x) ==> Under_watering(x)'))
    KB.tell(expr('Outdoor(x) & Sometimes(x) & Yellow_leaves(x) ==> Underwatering_or_nutrient_deficiency(x)'))

    KB.tell(expr('Outdoor(x) & Fungus(x) ==> Poor_air_Circulation(x)'))

    issues = [
        'Root_rot_over_watering',
        'Lack_of_fertilizing',
        'Not_exposed_to_lighting',
        'Lack_of_ventilation_humidity',
        'Chemical_exposure',
        'Under_watering_lack_of_fertilizing',
        'Under_watering_not_exposed_to_lighting',
        'Lack_of_ventilation',
        'Improper_drainage',
        'Overwatering',
        'Lack_of_nutrients_particularly_nitrogen',
        'Chemical_exposure_improper_drainage',
        'Under_watering',
        'Underwatering_or_nutrient_deficiency',
        'Poor_air_Circulation'
    ]

    responces = [
        'Root rot or over watering',
        'Lack of fertilizing',
        'Not exposed to lighting',
        'Lack of ventilation and humidity',
        'Chemical exposure',
        'Under watering or lack of fertilizing',
        'Under watering or not exposed to lighting',
        'Lack of ventilation',
        'Improper drainage',
        'Overwatering',
        'Lack of nutrients particularly nitrogen',
        'Chemical exposure or improper drainage',
        'Under watering',
        'Underwatering or nutrient deficiency',
        'Poor air circulation'
    ]


    find = False
    for i in range(len(issues)):

        result = list(fol_bc_ask(KB, expr(f'{issues[i]}(x)')))

        if result:
            find = True
            data = {
                'issue': responces[i]
            }
            print(data)

            return JsonResponse(data)

    """if find == False:
        return JsonResponse({'issue': "There are no issues"})"""



