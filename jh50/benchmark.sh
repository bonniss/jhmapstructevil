#!/bin/sh

get_timestamp_ms() {
    if [[ "$OSTYPE" == "darwin"* ]]; then
        # macOS, use gdate (GNU date installed via brew)
        if command -v gdate >/dev/null 2>&1; then
            gdate +%s%3N
        else
            echo "Error: gdate is not installed. Please install it using 'brew install coreutils'."
            exit 1
        fi
    else
        # Linux
        date +%s%3N
    fi
}

print_parent_folder() {
    local parent_folder=$(basename "$PWD")
    echo "=================="
    echo "$parent_folder"
    echo "=================="
}

function print_ms_to_secs() {
  printf "%.3f\n" $(awk "BEGIN {print \"$1\" / 1000}")
}

# Function to calculate average time
calculate_average_time() {
    local total_time=0
    local runs=$1
    shift
    local times=("$@")

    for time in "${times[@]}"; do
        total_time=$((total_time + time))
    done

    average_build_time=$(($total_time / $runs))

    print_ms_to_secs $average_build_time
}

# Function to measure build time for an app
measure_build_time() {
    local app_dir=$1
    local runs=$2
    local times=()

    echo "Measuring build time for $app_dir..."

    for ((i=1; i<=runs; i++)); do
        start_time=$(get_timestamp_ms)

        # Run the build (skip tests and use cached dependencies)
        cd "$app_dir" || exit
        chmod +x ./mvnw
        ./mvnw clean install -Dmaven.test.skip=true > /dev/null 2>&1
        cd - > /dev/null 2>&1

        end_time=$(get_timestamp_ms)

        # build time in milliseconds
        build_time=$(($end_time - $start_time))

        times+=("$build_time")

        printf "Run %d: %.3f seconds\n" $i $(awk "BEGIN {print \"$build_time\" / 1000}")
    done

    calculate_average_time "$runs" "${times[@]}"
}

# Number of times to run the build
RUNS=3

print_parent_folder

echo
echo '~~~~~~~~~~~~~~~~~~'
no_dto_avg_time=$(measure_build_time "no-dto" "$RUNS")
echo "$no_dto_avg_time seconds"
echo '~~~~~~~~~~~~~~~~~~'

echo
echo '~~~~~~~~~~~~~~~~~~'
with_dto_avg_time=$(measure_build_time "with-dto" "$RUNS")
echo "$with_dto_avg_time seconds"
echo '~~~~~~~~~~~~~~~~~~'
