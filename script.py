from dataclasses import dataclass

@dataclass
class TestInformation:
    file_name: str
    bitset_sequence: str
    objective_value: float
    number_of_sensors: int = 0


file_paths = [
    "C:/Users/doguk/Desktop/targus/greedy_selection_algorithm_benchmark_tests.txt",
    "C:/Users/doguk/Desktop/targus/standard_ga_benchmark_tests.txt",
    "C:/Users/doguk/Desktop/targus/improved_ga_benchmark_tests.txt",
]

def analyze(file_path=""):
    best_bitset_sequences = list()
    best_objective_values = list()

    bitset_sequence = ""
    objective_value = 0.0
    with open(file_path) as file:
        while True:
            line = file.readline()
            if not line:
                break

            if "k" in line.lower() or "m" in line.lower():
                if bitset_sequence != "":
                    best_bitset_sequences.append(bitset_sequence)
                if objective_value > 0:
                    best_objective_values.append(objective_value)
                continue

            if line.startswith("{"):
                bitset_sequence = line.strip()
            if line.startswith("0."):
                objective_value = float(line.strip())

    least_sensor_sequence = best_bitset_sequences[0]
    least_number_of_sensors = len(least_sensor_sequence.split(","))
    for sequence in best_bitset_sequences:
        temp = len(sequence.split(","))
        if temp < least_number_of_sensors:
            least_sensor_sequence = sequence

    return TestInformation(
        file_path.split("/")[-1],
        least_sensor_sequence,
        sum(best_objective_values) / len(best_objective_values),
        least_number_of_sensors
    )

for file_path in file_paths:
    info = analyze(file_path)
    print(info, end="\n\n")
