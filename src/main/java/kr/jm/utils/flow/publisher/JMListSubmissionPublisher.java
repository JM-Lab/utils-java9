package kr.jm.utils.flow.publisher;

import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMOptional;

import java.util.List;

/**
 * The type Jm list submission publisher.
 *
 * @param <T> the type parameter
 */
public class JMListSubmissionPublisher<T> extends
        JMSubmissionPublisher<List<T>> {

    @Override
    public int submit(List<T> itemList) {
        JMLog.debug(log, "submit", itemList);
        return JMOptional.getOptional(itemList).map(super::submit)
                .orElse(0);
    }

}
