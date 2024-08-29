bazel build \
  --profile=profile.json.gz \
  --experimental_profile_include_target_label \
  --disk_cache= \
  --noremote_accept_cached \
  //...
