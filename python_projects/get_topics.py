import re

input_filename = "full_docker_message.json"
time_zone_regex = re.compile('"time_zone": "?([^,"]+)"?,')

match_dict = {}
if __name__ == '__main__':
    with open(input_filename, 'r', encoding="utf8") as f:
        contents = f.read()

    time_zone_matches = time_zone_regex.finditer(contents)
    for match in time_zone_matches:
        if match.group(1) not in match_dict:
            match_dict[match.group(1)] = 1
        else:
            match_dict[match.group(1)] += 1


    print(match_dict)
    # Sort the dictionary by value
    sorted_match_dict = sorted(match_dict.items(), key=lambda kv: kv[1], reverse=True)
    print(sorted_match_dict)
    print("{")
    for key, value in sorted_match_dict:
        print(f'"{key}",')
    print("}")

