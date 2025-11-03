#!/usr/bin/python3

import random
import json
import string

def get_fight_valid():
    styles = [
        "BJJ"
        , "Karate"
        , "Judo"
        , "KungFu"
        , "Capoeira"
        , "Boxing"
        , "Taekwondo"
        , "Aikido"
        , "KravMaga"
        , "MuayThai"
        , "KickBoxing"
        , "Pankration"
        , "Wrestling"
        , "Sambo"
        , "Savate"
        , "Sumo"
        , "Kendo"
        , "Hapkido"
        , "LutaLivre"
        , "WingChu"
        , "Ninjutsu"
        , "Fencing"
        , "ArmWrestling"
        , "SuckerPunch"
        , "44Magnum"]
    tem_fight = random.choice([i != 11  for i in range(1, 15)])
    fight_values = None
    if tem_fight:
        fight_values = random.choices(styles, k=random.randint(1, len(styles) + 1))
    return fight_values

def get_name_valid():
    return ''.join(random.choice(string.ascii_letters + ' ') for i in range(100))[:random.randint(1, 100)]

def get_dob_valid():
    return str(random.randint(1940, 2020)) + '-' + str(random.randint(1, 12+1)).rjust(2, '0') + '-' + str(random.randint(1, 28+1)).rjust(2, '0')

def get_fight_invalid():
    return random.choice([["1111111111111111111111111111111111111111"], "string", 1])

def get_nick_invalido():
    return ''.join(random.choice(string.ascii_letters) for i in range(35))

def get_name_invalid():
    return ''.join(random.choice(string.ascii_letters + ' ') for i in range(120))

def get_dob_invalid():
    return json.dumps(random.choice(["12-12-2000", None, 10, "?!?!?"]))

def get_payload():
    generate_valid_name = random.choice([i != 1  for i in range(1, 100)])
    get_name = get_name_valid if generate_valid_name else get_name_invalid
    
    generate_dob_valid = random.choice([i != 1  for i in range(1, 100)])
    get_dob = get_dob_valid if generate_dob_valid else get_dob_invalid
    
    generate_fight_valid = random.choice([i != 1  for i in range(1, 100)])
    get_fight = get_fight_valid if generate_fight_valid else get_fight_invalid

    payload = {"name" : get_name(), "dob" : get_dob(), "fight_skills" : get_fight()}
    return json.dumps(payload)

def get_search_term():
    t = ''.join(random.choice(string.ascii_letters + ' ') for i in range(100))[:random.randint(1, 50)]
    return t.strip() or 'x'

def generate_payload(numero_registros):
    with open('stress-test/user-files/resources/warriors-payloads.tsv', 'w+') as f:
        f.write("payload\n")
        for _ in range(numero_registros):
            f.write(f"{get_payload()}\n")


def generate_search_terms(numero_registros):
    with open('stress-test/user-files/resources/search-terms.tsv', 'w+') as f:
        f.write("t\n")
        for _ in range(numero_registros):
            f.write(get_search_term() + "\n")

generate_payload(100_000)
generate_search_terms(5_000)
