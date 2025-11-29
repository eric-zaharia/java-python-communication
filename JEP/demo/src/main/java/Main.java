    import jep.JepConfig;
    import jep.SharedInterpreter;

    import java.util.Map;

    public class Main {
        private static final Map<String, Object> DEFAULT_PAYLOAD = Map.of("input", "static_payload");
        static {
            JepConfig config = new JepConfig();
            config.redirectStdout(System.out);
            config.redirectStdErr(System.err);
            SharedInterpreter.setConfig(config);
        }

        static {
            JepConfig config = new JepConfig().addIncludePaths("../python");

             config.redirectStdout(System.out);
             config.redirectStdErr(System.err);

            SharedInterpreter.setConfig(config);
        }

        public static void main(String[] args) {
            try (SharedInterpreter interp = new SharedInterpreter()) {
                interp.exec("import inference");

                Object result = interp.invoke(
                        "inference.run_inference",
                        new Object[]{ DEFAULT_PAYLOAD }
                );

                @SuppressWarnings("unchecked")
                Map<String, Object> output = (Map<String, Object>) result;

                System.out.println("output returned to Java: " + output);


                Object result2 = interp.invoke(
                        "inference.run_inference_pip_package",
                        new Object[]{ DEFAULT_PAYLOAD }
                );

                Double output2 = (Double) result2;
                System.out.println("output returned to Java, using pip installed package: " + output2);
            }
        }
    }
